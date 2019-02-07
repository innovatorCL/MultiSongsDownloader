package com.innovator.multisongsdownloader.network;

import java.util.List;

public interface GetSongsInfoCallback extends GetSongCallback{


        void onSuccess(List<SongInfo> songs);

        void onFail(String msg, int code);

}
