package com.example.multimediaplayer.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.multimediaplayer.Items.Video;
import com.example.multimediaplayer.R;
import com.example.multimediaplayer.Utils.ScanFiles;
import com.example.multimediaplayer.Utils.VideoAdapter;

import java.util.ArrayList;
import java.util.List;


public class VideoListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        initView();//控件初始化
        initData();//数据初始化
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        recyclerView=findViewById(R.id.recyclerview);
    }

    //数据初始化，将扫描出的视频文件转成Video类List,添加到VideoAdapter
    private void initData() {
        ScanFiles scanFiles = new ScanFiles(VideoListActivity.this);//读取sd文件功能实例
        //准备视频数据
        List<Video> videolist=new ArrayList<>();
        String[] url;

        for(String s : scanFiles.getVideoPaths()){
            url = s.split("/");
            videolist.add(new Video(url[url.length-1],s));
        }
        //适配器
        VideoAdapter adapter=new VideoAdapter(videolist);
        //让数据显示到 recyclerview 控件上
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}