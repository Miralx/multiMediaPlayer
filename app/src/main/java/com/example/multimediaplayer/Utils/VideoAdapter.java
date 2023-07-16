package com.example.multimediaplayer.Utils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multimediaplayer.Activity.VideoPlayActivity;
import com.example.multimediaplayer.Items.Video;
import com.example.multimediaplayer.R;

import java.util.List;
//显示视频列表的适配器
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private List<Video> videolist;

    //传入VideoList
    public VideoAdapter(List<Video> videolist) {
        this.videolist = videolist;
    }

    //方法 1：用于创建 ViewHolder 实例
    //将布局转化为View并传递给RecyclerView封装好的ViewHolder
    //在onCreateViewHolder()中的第二个参数，就是进行类别区分的，和ListView类似，
    //需要在Adapter内部重写getItemViewType(intposition) 方法，实现哪个位置需要加载哪种布局的逻辑。
    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater()是用来找res/layout/下的xml布局文件，并且实例化
        //inflate()方法去加载一个布局
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item,parent,false);

        final ViewHolder holder=new ViewHolder(view);

        holder.videoview.setOnClickListener(view1 -> {
            //单击任意视频跳转到播放界面
            int position=holder.getAdapterPosition();
            Video video =videolist.get(position);
            String myvideoname=video.getVideoName();
            String myvideoUrl=video.getVideoUrl();
            Intent intent=new Intent(view.getContext(), VideoPlayActivity.class);
            intent.putExtra("videoname",myvideoname);
            intent.putExtra("videourl",myvideoUrl);
            view.getContext().startActivity(intent);
        });
        return holder;
    }

    //方法 2：用于对 Recyclerview 中子项的数据进行赋值的
    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        Video video=videolist.get(position);
        holder.video_name.setText(video.getVideoName());
    }

    //方法 3：返回 Recyclerview 中数据源长度
    @Override
    public int getItemCount() {
        return videolist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView video_name;
        View videoview;
        public ViewHolder(@NonNull View view) {
            super(view);
            video_name=view.findViewById(R.id.video_name);
            videoview=view;
        }
    }


}


