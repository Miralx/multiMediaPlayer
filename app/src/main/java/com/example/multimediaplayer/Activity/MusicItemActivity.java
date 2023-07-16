package com.example.multimediaplayer.Activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multimediaplayer.Audio.MusicAdapter;
import com.example.multimediaplayer.R;
import com.example.multimediaplayer.Utils.ScanFiles;

public class MusicItemActivity extends Activity {

    RecyclerView musicRv;
    //数据源
    ScanFiles scanFiles;
    private MusicAdapter adapter;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_item);

        mediaPlayer = new MediaPlayer();
        scanFiles = new ScanFiles(MusicItemActivity.this);  //新建对象进行扫描文件
        musicRv = findViewById(R.id.recyclerview);
        //创建适配器
        adapter = new MusicAdapter(this, ScanFiles.getmDatas());
        //设置适配器
        musicRv.setAdapter(adapter);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        musicRv.setLayoutManager(layoutManager);
    }

}
