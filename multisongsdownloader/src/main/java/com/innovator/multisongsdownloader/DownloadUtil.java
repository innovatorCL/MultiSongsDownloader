package com.innovator.multisongsdownloader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ActionMenuView;
import android.widget.Toast;

import com.innovator.multisongsdownloader.network.GetSongUrlCallback;
import com.innovator.multisongsdownloader.network.GetSongsInfoCallback;
import com.innovator.multisongsdownloader.network.NetWorkTools;
import com.innovator.multisongsdownloader.network.Request;
import com.innovator.multisongsdownloader.network.SongInfo;
import com.innovator.multisongsdownloader.network.URLFileSaver;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadUtil {

    public static String queryString = "jQuery111309041138037361462";

    private ExecutorService mExecutorService;

    private static DownloadUtil instance;

    private static Context mContext;


    private DownloadUtil(Context context) {
        mContext = context.getApplicationContext();
        mExecutorService = Executors.newFixedThreadPool(5);

    }

    public static DownloadUtil getInstance(Context context){
        if (instance == null) {
            synchronized (DownloadUtil.class) {
                if (instance == null) {
                    instance = new DownloadUtil(context);
                }
            }
        }
        return instance;
    }
    /**
     * 通过歌名获取歌曲ID
     *
     */
    public void getSongIdByName(final String searchName,final String songName,final String artist){

        String url = "http://music.zhuolin.wang/api.php?callback="+queryString+"_"+System.currentTimeMillis();

        String body = "types=search&count=20&source=baidu&pages=1&name="+searchName;

        Request request = new Request.Builder(url)
                .method("POST")
                .addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                .addBody(body)
                .build();

        NetWorkTools.getSingleCase(mContext).enqueue(request, new GetSongsInfoCallback() {
            @Override
            public void onSuccess(List<SongInfo> songs) {

                for(int i = 0;i<songs.size();i++){
                    if(songName.equals(songs.get(i).getName())){
                        //更精准匹配
                        for(String s:songs.get(i).getArtist()){
                            if(artist.equals(s)){
//                                Log.i("LogUtils","歌曲 ID："+songs.get(i).getId());
                                getSongUrlById(songs.get(i).getId(),searchName);
                                return;
                            }
                        }

                    }
                }

                Log.i("LogUtils","查不到这首歌："+searchName);
            }

            @Override
            public void onFail(String msg, int code) {

            }
        });

    }

    /**
     * 通过ID获取下载url
     * @param id
     */
    private void getSongUrlById(String id, final String fileName){

        String url = "http://music.zhuolin.wang/api.php?callback=jQuery111309041138037361462_"+System.currentTimeMillis();

        String body = "types=url&id="+id+"&source=baidu";

        Request request = new Request.Builder(url)
                .method("POST")
                .addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                .addBody(body)
                .build();

        NetWorkTools.getSingleCase(mContext).enqueue(request, new GetSongUrlCallback() {
            @Override
            public void onSuccess(String url) {

                String songName = fileName;
                downloadSong(url,songName);
            }

            @Override
            public void onFail(String msg, int code) {

            }
        });
    }

    /**
     * 下载歌曲
     * @param url
     */
    private void downloadSong(String url,String fileName){
        Log.i("LogUtils","下载歌曲："+fileName+"，url: "+url);
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Runnable runnable = new URLFileSaver(url,fileName);
        mExecutorService.execute(runnable);
    }

}
