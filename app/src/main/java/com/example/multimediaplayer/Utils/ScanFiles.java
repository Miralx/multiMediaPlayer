package com.example.multimediaplayer.Utils;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.multimediaplayer.Audio.MusicBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//搜索sd卡上所有MP4、3gp格式的视频，mp3、aac格式的音频的类
//动态申请权限在应用启动时添加了，要做本功能类单独测试可以把动态申请的代码注释加进去
//scanVideoFiles搜索视频，scanAudioFiles搜索音频

public class ScanFiles {
    private ArrayList<String> videoPaths;//所有视频文件路径
    private ArrayList<String> mp4Paths;//mp4格式文件路径
    private ArrayList<String> gpPaths;//3gp格式文件路径
    private ArrayList<String> audioPaths;//所有音频文件路径
    private ArrayList<String> mp3Paths;//mp3格式文件路径
    private ArrayList<String> aacPaths;//aac格式文件路径

    private static ArrayList<MusicBean> mDatas;//音频文件的MusicBean对象

    public ScanFiles(Context context){
        videoPaths = new ArrayList<>();
        mp4Paths = new ArrayList<>();
        gpPaths = new ArrayList<>();
        audioPaths = new ArrayList<>();
        mp3Paths = new ArrayList<>();
        aacPaths = new ArrayList<>();
        mDatas = new ArrayList<>();
        scanVideoFiles(context);
        scanAudioFiles(context);
    }
    //扫描所有视频文件，type=0返回所有, type=1返回mp4, type=2返回3gp
    public void scanVideoFiles(Context context){
        //初始清空列表
        videoPaths.clear();
        mp4Paths.clear();
        gpPaths.clear();
        //创建一个ContentResolver对象，用来访问媒体数据库
        ContentResolver contentResolver = context.getContentResolver ();

        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query (videoUri, null, null, null, null);

        //判断Cursor是否为空
        if (cursor != null) {
            //遍历Cursor中的每一行数据
            while (cursor.moveToNext ()) {
                Uri audio = ContentUris.withAppendedId (videoUri, cursor.getLong (cursor.getColumnIndexOrThrow (MediaStore.Video.Media._ID)));
                String path = cursor.getString (cursor.getColumnIndexOrThrow (MediaStore.Video.Media.DATA));
                if (path != null ) {
                    videoPaths.add(path);
                    if(path.endsWith (".3gp")) {
                        gpPaths.add(path);
                    }
                    else if(path.endsWith(".mp4")){
                        mp4Paths.add(path);
                    }
                }
            }
            //关闭Cursor
            cursor.close ();
        }

    }



    //扫描所有音频文件, type=0返回所有, type=1返回mp3, type=2返回aac
    public void scanAudioFiles(Context context){
        //初始清空列表
        audioPaths.clear();
        mp3Paths.clear();
        aacPaths.clear();
        mDatas.clear();

        //创建一个ContentResolver对象，用来访问媒体数据库
        ContentResolver contentResolver = context.getContentResolver ();

        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query (audioUri, null, null, null, null);

        //判断Cursor是否为空
        if (cursor != null) {
            //遍历Cursor中的每一行数据
            int id = 0;
            while (cursor.moveToNext ()) {
                Uri audio = ContentUris.withAppendedId (audioUri, cursor.getLong (cursor.getColumnIndexOrThrow (MediaStore.Audio.Media._ID)));
                String path = cursor.getString (cursor.getColumnIndexOrThrow (MediaStore.Audio.Media.DATA));
                String song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                id++;
                String sid = String.valueOf(id);
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                String time = sdf.format(new Date(duration));
                //将数据封装到对象
                MusicBean tempBean = new MusicBean(sid, song, singer, path, time);

                if (path != null ) {
                    audioPaths.add(path);
                    if(path.endsWith (".aac")) {
                        aacPaths.add(path);
                        mDatas.add(tempBean);
                    }
                    else if(path.endsWith(".mp3")){
                        mp3Paths.add(path);
                        mDatas.add(tempBean);
                    }
                }
            }
            //关闭Cursor
            cursor.close ();
        }

    }

    public ArrayList<String> getVideoPaths() {
        return videoPaths;
    }

    public ArrayList<String> getMp4Paths() {
        return mp4Paths;
    }

    public ArrayList<String> getGpPaths() {
        return gpPaths;
    }
    public ArrayList<String> getAudioPaths() {
        return audioPaths;
    }

    public ArrayList<String> getMp3Paths() {
        return mp3Paths;
    }

    public ArrayList<String> getAacPaths() {
        return aacPaths;
    }

    public static ArrayList<MusicBean> getmDatas() {
        return mDatas;
    }
}

