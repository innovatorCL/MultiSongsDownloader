package com.innovator.multisongsdownloader.network;

public interface GetSongUrlCallback extends GetSongCallback{

        void onSuccess(String url);

        void onFail(String msg, int code);
}
