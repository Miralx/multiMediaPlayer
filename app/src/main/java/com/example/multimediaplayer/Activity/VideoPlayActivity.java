package com.example.multimediaplayer.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;
import android.Manifest;

import com.example.multimediaplayer.R;

//AppCompatActivity
public class VideoPlayActivity extends AppCompatActivity {


    //定义对象
    private boolean display;//是否开始播放的状态标志
    private TextView my_videoname;//视频名称
    private VideoView my_videoview; //视频播放器
    private MediaController mediaController;//定义媒体控制柄
    private ImageView start_pause_img;//屏幕中的暂停按钮
    private ImageButton start_pause_imgButton;//暂停imgButton按钮
    private ImageButton return_imgButton;//返回视频列表
    private SeekBar seekbar;
    private TextView left_time, right_time;

    private MediaMetadataRetriever mmr;
    private int video_time;
    private Handler handler;
    private Runnable runnable;
    //seekbar点击事件
    private SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            left_time.setText(showTime(seekBar.getProgress()));
            right_time.setText(showTime(video_time-seekBar.getProgress()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            Log.i("进度", Integer.toString(progress));
            if(my_videoview != null && my_videoview.isPlaying()){
                my_videoview.seekTo(progress);
            }
        }
    };

    private void initView() {
        display = false;
        my_videoname=findViewById(R.id.video_name);
        my_videoview=findViewById(R.id.VideoView);
        //addr = findViewById(R.id.addr);
        mediaController=new MediaController(this);
        start_pause_img=findViewById(R.id.img_play);
        start_pause_imgButton=findViewById(R.id.stop_button);
        return_imgButton=findViewById(R.id.back_img);
        seekbar=findViewById(R.id.play_seekbar);
        handler=new Handler(Looper.myLooper());
        left_time=findViewById(R.id.video_left_time_text);
        right_time=findViewById(R.id.video_right_time_text);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initView();//控件初始化
        initData();//数据初始化
        setLitsener();//设置点击监听器
        seekbar.setOnSeekBarChangeListener(changeListener);


    }
    //时间显示函数,我们获得音乐信息的是以毫秒为单位的，把把转换成我们熟悉的00:00格式
    public String showTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }
    private void setLitsener(){
        //停止按钮点击事件
        start_pause_imgButton.setOnClickListener(view -> {
            //Log.i("durance_time","durancetime = "+my_videoview.getDuration());
            if(display){
                start_pause_img.setVisibility(view.VISIBLE);
                my_videoview.pause();
                display = false;
                start_pause_imgButton.setImageResource(R.drawable.play_button_pause_3);
            }
            else{
                start_pause_img.setVisibility(view.GONE);
                my_videoview.start();
                display = true;
                handler.postDelayed(runnable,500);
                start_pause_imgButton.setImageResource(R.drawable.play_button_start_2_black);
            }
        });
        //videoView点击停止事件
        my_videoview.setOnClickListener(view -> {
            if(display){
                start_pause_img.setVisibility(view.VISIBLE);
                my_videoview.pause();
                display = false;
            }
            else{
                start_pause_img.setVisibility(view.GONE);
                my_videoview.start();
                display = true;
            }
        });
        //返回按钮点击事件
        return_imgButton.setOnClickListener(view -> {
            Intent intent = new Intent(VideoPlayActivity.this, VideoListActivity.class);
            startActivity(intent);
        });
        my_videoview.setOnCompletionListener(mediaPlayer -> {//为MediaPlayer的播放完成事件绑定监听

        });
        my_videoview.setOnErrorListener((mediaPlayer, i, i1) -> {//当播放过程中出现错误时，该listener可以收到通知
            /*
            错误常数
                MEDIA_ERROR_IO
                文件不存在或错误，或网络不可访问错误
                值: -1004 (0xfffffc14)
                MEDIA_ERROR_MALFORMED
                流不符合有关标准或文件的编码规范
                值: -1007 (0xfffffc11)
                MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK
                视频流及其容器不适用于连续播放视频的指标（例如：MOOV原子）不在文件的开始.
                值: 200 (0x000000c8)
                MEDIA_ERROR_SERVER_DIED
                媒体服务器挂掉了。此时，程序必须释放MediaPlayer 对象，并重新new 一个新的。
                值: 100 (0x00000064)
                MEDIA_ERROR_TIMED_OUT
                一些操作使用了过长的时间，也就是超时了，通常是超过了3-5秒
                值: -110 (0xffffff92)
                MEDIA_ERROR_UNKNOWN
                未知错误
                值: 1 (0x00000001)
                MEDIA_ERROR_UNSUPPORTED
                比特流符合相关编码标准或文件的规格，但媒体框架不支持此功能
                值: -1010 (0xfffffc0e)
                what int: 标记的错误类型:
                    MEDIA_ERROR_UNKNOWN
                    MEDIA_ERROR_SERVER_DIED
                extra int: 标记的错误类型.
                    MEDIA_ERROR_IO
                    MEDIA_ERROR_MALFORMED
                    MEDIA_ERROR_UNSUPPORTED
                    MEDIA_ERROR_TIMED_OUT
                    MEDIA_ERROR_SYSTEM (-2147483648) - low-level system error.
            * */
            if(i==MediaPlayer.MEDIA_ERROR_SERVER_DIED){
                //媒体服务器挂掉了。此时，程序必须释放MediaPlayer 对象，并重新new 一个新的。
                Toast.makeText(VideoPlayActivity.this,
                        "网络服务错误",
                        Toast.LENGTH_LONG).show();
            }else if(i==MediaPlayer.MEDIA_ERROR_UNKNOWN){
                if(i1==MediaPlayer.MEDIA_ERROR_IO){
                    //文件不存在或错误，或网络不可访问错误
                    Toast.makeText(VideoPlayActivity.this,
                            "网络文件错误",
                            Toast.LENGTH_LONG).show();
                } else if(i1==MediaPlayer.MEDIA_ERROR_TIMED_OUT){
                    //超时
                    Toast.makeText(VideoPlayActivity.this,
                            "网络超时",
                            Toast.LENGTH_LONG).show();
                }
            }
            //initData();
            return false;
        });
    }

    private void initData() {

        runnable = () -> {
            int position = my_videoview.getCurrentPosition();
            int totaldurance = my_videoview.getDuration();
            if(my_videoview.isPlaying()){
                seekbar.setMax(totaldurance);
                seekbar.setProgress(position);
            }
            handler.postDelayed(runnable,500);
        };

        //1、获取从音乐列表传过来的视频名称和视频地址
        String myvideoname=getIntent().getStringExtra("videoname");
        String myvideourl=getIntent().getStringExtra("videourl");
        video_time = getVideoDurance(myvideourl);
        //String path= Environment.getExternalStorageDirectory().getAbsolutePath()+myvideoname+".mp4";
        //addr.setText(myvideourl);
        int dur;
        Log.i("PlayActivity","path="+myvideourl);

        //2、将视频名称显示在文本框中，将视频地址关联到播放器中
        if(myvideoname.isEmpty()){
            Log.i("name", "name is null");
        }
        else{
            Log.i("name", "name="+myvideoname);
        }
        my_videoname.setText(myvideoname);
        my_videoview.setVideoPath(myvideourl);
        //视频播放器和媒体控制柄关联起来
        my_videoview.setMediaController(mediaController);
        //媒体控制柄和视频播放器关联起来
        mediaController.setMediaPlayer(my_videoview);
        //3、启动视频播放器播放视频
        //my_videoview.start();


    }

    public int getVideoDurance(String path){
        //1，创建MediaMetadataRetriever对象
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //2.设置音视频资源路径
        retriever.setDataSource(path);
        //3.获取音视频资源总时长
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        Log.i("video_durance","time = "+time);
        return Integer.parseInt(time);
    }

    //public String secondToMinute(int)
}