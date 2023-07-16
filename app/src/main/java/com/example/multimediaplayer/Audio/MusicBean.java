package com.example.multimediaplayer.Audio;

public class MusicBean {
    private String song_id;//歌曲id
    private String song_name;//歌曲名称
    private String singer_id;//歌手
    private String path;//路径

    private  String time;//时长


    public MusicBean() {//空参构造
    }

    public MusicBean(String song_id, String song_name, String singer_id, String path, String time) {//全参
        this.song_id = song_id;
        this.song_name = song_name;
        this.singer_id = singer_id;
        this.path = path;
        this.time = time;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getSinger_id() {
        return singer_id;
    }

    public void setSinger_id(String singer_id) {
        this.singer_id = singer_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
