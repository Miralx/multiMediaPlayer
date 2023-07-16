package com.example.multimediaplayer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.multimediaplayer.R;

public class PartitionActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partition);

        ImageView videoImg = findViewById(R.id.video_img);
        videoImg.setOnClickListener(v -> {
            Intent intent = new Intent(PartitionActivity.this, VideoListActivity.class);
            startActivity(intent);
        });
        ImageView musicImg = findViewById(R.id.music_img);
        musicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PartitionActivity.this, MusicItemActivity.class);
                startActivity(intent);
            }
        });
    }
}
