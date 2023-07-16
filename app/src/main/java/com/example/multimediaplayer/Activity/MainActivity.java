package com.example.multimediaplayer.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.multimediaplayer.R;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //在 Manifest加入 <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //请求授权，接受三个参数：Activity实例；请求权限名的String数组；请求码（唯一值即可）
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        //从载入界面切换到主界面
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //切换页面的两种方法
                Intent intent = new Intent(MainActivity.this, PartitionActivity.class);
                startActivity(intent);
            }
        });
    }
}