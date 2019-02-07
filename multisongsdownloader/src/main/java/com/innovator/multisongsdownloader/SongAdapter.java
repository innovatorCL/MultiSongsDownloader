package com.innovator.multisongsdownloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{

    private List<String> mSongList;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;

    public SongAdapter(Context context, List<String> list){
        this.mContext = context;
        this.mSongList = list;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.list_item_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String[] temp = mSongList.get(i).split(" ");
        String songName = "";
        String artistName = "";
        if(temp.length > 1){
            songName = temp[0];
            artistName = temp[1];
        }
        viewHolder.songNameTV.setText("歌曲名 "+songName);
        viewHolder.artistNameTV.setText("歌手名 "+artistName);
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView songNameTV;
        private TextView artistNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            songNameTV = itemView.findViewById(R.id.song_name_tv);
            artistNameTV = itemView.findViewById(R.id.artist_name_tv);
        }
    }
}
