package com.example.multimediaplayer.Activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multimediaplayer.Audio.MusicBean;
import com.example.multimediaplayer.R;
import com.example.multimediaplayer.Utils.ScanFiles;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayActivity extends Activity implements View.OnClickListener{
    //控件
    ImageView nextIv,lastIv, playIv;
    TextView singerTv,songTv, nowTime, allTime;
    SeekBar seekBar;

    //资源
    ArrayList<MusicBean> mDatas;

    //记录当前正在播放音乐的位置
    int currentPlayPosition = -1;

    //记录暂停音乐时进度条的位置
    int currentPausePositionInSong = 0;

    MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_play);

        mediaPlayer = new MediaPlayer();

        //获取资源
        mDatas = ScanFiles.getmDatas();   //已经扫描过了，直接获取

        //控件初始化
        initView();

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0); //没传就取第一首
        currentPlayPosition = position;

        //首先播放所点击的歌曲
        playMusicInMusicBean(mDatas.get(position));
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

    private void initView(){
        nextIv = findViewById(R.id.next_music_img);
        playIv = findViewById(R.id.music_Play_img);
        lastIv = findViewById(R.id.last_music_img);
        singerTv = findViewById(R.id.TextName);
        songTv = findViewById(R.id.TextTitle);
        nowTime = findViewById(R.id.TextStart_time);
        allTime = findViewById(R.id.TextEnd_time);

        seekBar = findViewById(R.id.PlayLine);

        nextIv.setOnClickListener(this);//设置点击事件
        playIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            //更新positionBar
            seekBar.setProgress(currentPosition);
        }
    };

    public void playMusicInMusicBean(MusicBean musicBean) {//封装
        /* 根据传入对象播放音乐*/
        //设置显示的歌手名与歌曲名
        singerTv.setText(musicBean.getSinger_id());
        songTv.setText(musicBean.getSong_name());
        allTime.setText(musicBean.getTime());
        nowTime.setText(showTime(0));
        stopMusic();
        //重置多媒体播放器，更改地址
        mediaPlayer.reset();

        seekBar.setProgress(0);

        //设置新路径
        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }

        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // 设置进度条拖动事件
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                nowTime.setText(showTime(progress));
                if (fromUser) {
                    mediaPlayer.seekTo(progress); // 改变音乐播放位置
                    seekBar.setProgress(progress);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                nowTime.setText(showTime(seekBar.getProgress()));
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run () {
                while (mediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);

                        Thread.sleep(1000);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    /*
     *点击播放按钮播放音乐或暂停音乐重新播放
     * 1.从暂停到播放
     * 2.从停止到播放
     */
    private void playMusic() {
        if (mediaPlayer!=null && !mediaPlayer.isPlaying()) {
            if (currentPausePositionInSong == 0) {
                try {
                    mediaPlayer.prepare();
//                    mediaPlayer.start();   //不能直接start 可能没prepare好
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                //从暂停到播放
                mediaPlayer.seekTo(currentPausePositionInSong);
//                mediaPlayer.start();
            }
            mediaPlayer.start();

            //播放音乐函数
            //1.暂停到播放(可能没实现) 2.停止到播放
            playIv.setImageResource(R.drawable.pause);//暂时用的play，少了暂停的图片
        }
    }


    private void pauseMusic() {
        //暂停音乐函数
        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
            currentPausePositionInSong = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            playIv.setImageResource(R.drawable.play);
        }
    }


    private void stopMusic() {
        //停止音乐的函数
        if(mediaPlayer!=null){
            currentPausePositionInSong = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            playIv.setImageResource(R.drawable.play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.last_music_img){
            if (currentPlayPosition == 0) {
                Toast.makeText(this,"已经是第一首了，没有上一曲!",Toast.LENGTH_SHORT).show();
                return;
            }
            currentPlayPosition = currentPlayPosition-1;
            MusicBean lastBean = mDatas.get(currentPlayPosition);
            playMusicInMusicBean(lastBean);
        }

        else if (v.getId() == R.id.next_music_img){
            if (currentPlayPosition == mDatas.size()-1) {
                Toast.makeText(this,"已经是最后一首了，没有下一曲!",Toast.LENGTH_SHORT).show();
                return;
            }
            currentPlayPosition = currentPlayPosition+1;
            MusicBean nextBean = mDatas.get(currentPlayPosition);
            playMusicInMusicBean(nextBean);
        }

        else if(v.getId() == R.id.music_Play_img){
            if (currentPlayPosition == -1) {
                //并没有选中要播放的音乐
                Toast.makeText(this,"请选择想要播放的音乐",Toast.LENGTH_SHORT).show();
                return;
            }
            if (mediaPlayer.isPlaying()) {
                //此时处于播放状态，需要暂停音乐
                pauseMusic();
            }else {
                //此时没有播放音乐，点击开始播放音乐
                playMusic();
            }
        }
    }
}
