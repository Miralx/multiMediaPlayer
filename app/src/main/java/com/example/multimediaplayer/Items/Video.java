package com.example.multimediaplayer.Items;
//储存单个视频的Video类
public class Video {
    private String videoName;
    private String videoUrl;

    public String getVideoName() {
        return videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Video(String videoName, String videoUrl) {
        this.videoName = videoName;
        this.videoUrl = videoUrl;

    }
}
