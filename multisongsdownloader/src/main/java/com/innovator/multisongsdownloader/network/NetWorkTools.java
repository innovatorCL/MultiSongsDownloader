package com.innovator.multisongsdownloader.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NetWorkTools {
    private static Context mContext;
    private static NetWorkTools mNetWorkTools = null;
    private ExecutorService mExecutorService;


    public static NetWorkTools getSingleCase(Context context) {

        if (mNetWorkTools == null) {
            synchronized (NetWorkTools.class) {
                if (mNetWorkTools == null) {
                    mNetWorkTools = new NetWorkTools(context);
                }
            }
        }
        return mNetWorkTools;
    }


    private NetWorkTools(Context context) {
        mContext = context.getApplicationContext();
        mExecutorService = Executors.newFixedThreadPool(3);

    }


    public void enqueue(final Request request, final GetSongCallback getSongsInfoCallback) {
        Runnable Worker = new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.i("LogUtils", "request.url() : " + request.url().toString());
//                    if (!TextUtils.isEmpty(request.body())) {
//                        Log.i("LogUtils", "request.body() : " + request.body().toString());
//                    }

                    HttpURLConnection conn = (HttpURLConnection) request.url().openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestMethod(request.method());
                    conn.setUseCaches(false);
                    conn.setConnectTimeout(50000);
                    conn.setReadTimeout(10000);
                    conn.setRequestProperty("Content_Type", "application/json");
                    if (request.headers() != null) {
                        Iterator iterator = request.headers().entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry entry = (Map.Entry) iterator.next();
                            //设置 Header
                            conn.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
//                            Log.i("LogUtils","key : " + (String) entry.getKey());
//                            Log.i("LogUtils","value  : " + (String) entry.getValue());
                        }
                    }
                    conn.connect();
                    conn.getOutputStream().write(request.body() != null ? request.body().getBytes() : new byte[0]);
                    conn.getOutputStream().flush();
                    conn.getOutputStream().close();

                    String msg;
                    int code = conn.getResponseCode();
//                    Log.i("LogUtils", "ResponseCode = " + code);
                    if (code == HttpURLConnection.HTTP_OK) {

                        msg = StreamUtils.readStream(conn.getInputStream());

                        if(msg.contains("[")){
//                            Log.i("LogUtils", "转到json数组处理");
                            int startIndex = msg.indexOf("[");
                            int endIndex = msg.lastIndexOf("]");
                            msg = msg.substring(startIndex,endIndex+1);
//                            Log.i("LogUtils", "处理后的数据 =" + msg);

                            JSONArray jsonArray = new JSONArray(msg);

                            List<SongInfo> songs = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                SongInfo info = new SongInfo();
                                JSONObject data = jsonArray.getJSONObject(i);
                                info.setId(data.getString("id"));
                                info.setName(data.getString("name"));
                                JSONArray temp = data.getJSONArray("artist");
                                List<String> artists = new ArrayList<>();
                                for(int j = 0; j < temp.length(); j++){
                                    artists.add((String)temp.get(j));
                                }
                                info.setArtist(artists);
                                songs.add(info);
                            }

                            if(getSongsInfoCallback != null && getSongsInfoCallback instanceof GetSongsInfoCallback){
                                ((GetSongsInfoCallback) getSongsInfoCallback).onSuccess(songs);
                            }

                        }else {
//                            Log.i("LogUtils", "转到json对象处理");
                            int startIndex = msg.indexOf("{");
                            int endIndex = msg.lastIndexOf("}");
                            msg = msg.substring(startIndex,endIndex+1);
//                            Log.i("LogUtils", "处理后的数据 =" + msg);

                            JSONObject object = new JSONObject(msg);
                            String url = object.getString("url");

                            if(getSongsInfoCallback != null && getSongsInfoCallback instanceof GetSongUrlCallback){
                                ((GetSongUrlCallback) getSongsInfoCallback).onSuccess(url);
                            }
                        }
                    } else {
                        msg = StreamUtils.readStream(conn.getErrorStream());
                        Log.i("LogUtils", "错误信息："+msg);
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    Log.i("LogUtils", "onFail : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        mExecutorService.execute(Worker);
    }

}
