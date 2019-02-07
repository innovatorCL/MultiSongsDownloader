package com.innovator.multisongsdownloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.innovator.multisongsdownloader.network.GetSongUrlCallback;
import com.innovator.multisongsdownloader.network.GetSongsInfoCallback;
import com.innovator.multisongsdownloader.network.NetWorkTools;
import com.innovator.multisongsdownloader.network.Request;
import com.innovator.multisongsdownloader.network.SongInfo;
import com.innovator.multisongsdownloader.network.URLFileSaver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    private EditText songEditText;
    private EditText artistEditText;
    private TextView addSongBtn;
    private TextView downloadBtn;
    private RecyclerView mListView;

    private List<String> data = new ArrayList<>();

    private SongAdapter mAdapter;

    //test
    private String[] mSongs = {
            "再见亦是朋友 曾航生","不装饰你的梦 蔡国权","冷冷的秋 甄妮","爱上你是我一生的错 何婉莹","是缘是债是场梦 刘锡明",
            "我曾用心爱着你 甄妮","你怎么舍得我难过 甄妮","再度孤独 甄妮","情比雨丝 关菊英","发誓 黎瑞思",
            "梦醒时分 陈淑桦","无声的雨 孟庭苇","到哪里找那么好的人 陈明真","我悄悄蒙上你的眼睛 陈艾湄","你的柔情我永远不懂 陈琳",
            "爱就爱了 陈琳","我要找到你 陈明","变心的翅膀 陈明真","我用自己的方式爱你 陈明真","背心 陈明真",
            "情债 陈明真","爱情的故事 方季惟","随缘 温兆伦","看透爱情看透你 冷漠","一曲红尘 冷漠",
            "心锁 冷漠","爱如星火 冷漠","情歌继续唱 冷漠","泪满天 龙梅子","今生有你 龙梅子",
            "一世夫妻 梅朵","留不住的温柔 李泽坚","久别的人 白雪","等你等了那么久 祁隆","我曾用心爱着你 潘美辰",
            "上海滩 叶丽仪","沉默是金 张国荣","风的季节 徐小凤","故乡的雨 徐小凤","顺流逆流 徐小凤",
            "每一步 徐小凤","风雨同路 徐小凤","人生何处不相逢 陈慧娴","活着就是等待 汤宝如","难忘初恋的情人 吕珊",
            "昨天，今天 ，下雨天 彭家丽","不装饰你的梦 王丽珍","一醉化千愁 温碧霞","千千阙歌 陈慧娴","逝去的诺言 陈慧娴",
            "夕阳之歌 梅艳芳","猛士的士高 fantasyBoy","不要离开我 猛土","请勿伤我心 猛土","醉醉的探戈 韩宝仪",
            "舞女泪 韩宝仪","错误的爱 韩宝仪","无奈的思绪 韩宝仪","缘 高胜美","山地情歌 高胜美",
            "青青河边草 高胜美","凡人歌 高胜美","有没有人曾告诉你 陈楚生","今夜你会不会来 黎明","晚秋 黄凯芹",
            "婉君 李翊君","惜别的海岸 龙飘飘","踏浪 徐怀钰","怎能再回头 龙飘飘","苦酒一杯又一杯 龙飘飘",
            "梦在你怀中 龙飘飘","是酒也是泪 李玲玉","雨中的恋人 黄凯芹","伤感的恋人 黄凯芹","总有你鼓励 李国祥",
            "一生不变 李克勤","爱的故事上集 孙耀威"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView(){

        songEditText = findViewById(R.id.song_edit_text);
        artistEditText = findViewById(R.id.artist_edit_text);
        addSongBtn = findViewById(R.id.add_song_btn);
        downloadBtn = findViewById(R.id.download_song_btn);
        mListView = findViewById(R.id.song_list_view);

        data = Arrays.asList(mSongs);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SongAdapter(MainActivity.this,data);
        mListView.setAdapter(mAdapter);

    }

    private void initListener(){
        //添加歌曲到列表，动态更新
        addSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(songEditText.getText()) && !TextUtils.isEmpty(artistEditText.getText())){
                    StringBuilder builder = new StringBuilder();
                    builder.append(songEditText.getText());
                    builder.append(" ");  //添加分隔符
                    builder.append(artistEditText.getText());
                    if(data.isEmpty() || !data.get(0).equals(builder.toString())){
                        data.add(0,builder.toString());
                        mAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(MainActivity.this,"已经添加过了 ",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"未输入歌曲名或者歌手名",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LogUtils","开始自动化操作");

                for(String s:data){
                    String songName = "";
                    String artist = "";
                    if (s.contains(" ")) {
                        String[] temp = s.split(" ");
                        songName = temp[0];
                        artist = temp[1];

                        Log.i("LogUtils", "歌曲名：" + songName + "，歌手：" + artist);
                    } else {
                        Log.i("LogUtils", "要搜索的名字不合法");
                    }
                    DownloadUtil.getInstance(MainActivity.this).getSongIdByName(s,songName,artist);

                }
            }
        });
    }
}
