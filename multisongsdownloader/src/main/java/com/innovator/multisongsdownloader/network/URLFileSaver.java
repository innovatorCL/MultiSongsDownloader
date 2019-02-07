package com.innovator.multisongsdownloader.network;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class URLFileSaver implements Runnable {

    private static final int BUFFER_SIZE = 4096;
    private String destUrl;
    private String fileName;

    private String sdCardPath;

    public URLFileSaver(String destUrl, String fileName) {
        this.destUrl = destUrl;
        this.fileName = fileName;
        this.sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyMusicDownload/";
    }

    public void run() {
//        try {
//            saveToFile(destUrl,fileName);
//            System.out.println("文件："+destUrl+"下载完成，保存为"+fileName);
//        } catch (IOException e) {
//            System.out.println("文件下载失败，信息："+e.getMessage());
//        }

        download(destUrl,fileName);
    }


    /**
     * 将网络文件保存为本地文件的方法
     * @param destUrl
     * @param fileName
     * @throws IOException
     */
    public void saveToFile(String destUrl, String fileName) throws IOException {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpconn = null;
        URL url = null;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;

        // 建立链接
        url = new URL(destUrl);
        httpconn = (HttpURLConnection) url.openConnection();
        // 连接指定的资源
        httpconn.connect();
        // 获取网络输入流
        bis = new BufferedInputStream(httpconn.getInputStream());


        // 建立文件
        fos = new FileOutputStream(fileName);

        Log.i("LogUtils","正在获取链接[" + destUrl + "]的内容\n将其保存为文件[" + fileName
                + "]");

        // 保存文件
        while ((size = bis.read(buf)) != -1){
            fos.write(buf, 0, size);
        }
        fos.close();
        bis.close();
        httpconn.disconnect();

    }

    //下载具体操作

    /**
     * 下载文件
     * @param downloadUrl
     * @param s
     */
    private void download(String downloadUrl, String s) {
        try {
            URL url = new URL(downloadUrl);
            //打开连接
            URLConnection conn = url.openConnection();
            //打开输入流
            InputStream is = conn.getInputStream();
            //获得长度
            int contentLength = conn.getContentLength();
//            Log.i("LogUtils", "contentLength = " + contentLength);
            //创建文件夹 MyMusicDownload，在存储卡下
//            String dirName = Environment.getExternalStorageDirectory() + "/MyMusicDownload/";
            File file = new File(sdCardPath);
            //不存在创建
            if (!file.exists()) {
                file.mkdir();
            }
            //下载后的文件名
            String fileName = sdCardPath + s+".mp3";
//            Log.i("LogUtils","正在获取链接[" + destUrl + "]的内容\n将其保存为文件[" + fileName
//                    + "]");
            File file1 = new File(fileName);
            if (file1.exists()) {
                file1.delete();
            }
            //创建字节流
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(file1);
            //写数据
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            //完成后关闭流
            Log.i("LogUtils", fileName+"下载完成");
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
