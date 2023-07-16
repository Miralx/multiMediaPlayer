package com.example.multimediaplayer.Audio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multimediaplayer.Activity.MusicPlayActivity;
import com.example.multimediaplayer.R;

import java.util.List;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    Context context;
    //数据源
    List<MusicBean>mDatas;

    public MusicAdapter(Context context, List<MusicBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }


    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_music,parent,false);//参数类型?

        MusicViewHolder holder = new MusicViewHolder(view);

        //为每个item绑定点击事件
        holder.songTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将当前歌曲位置发给播放活动
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(v.getContext(), MusicPlayActivity.class);
                intent.putExtra("position", position);
                v.getContext().startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        MusicBean musicBean = mDatas.get(position);
        holder.idTv.setText(musicBean.getSong_id());
        holder.songTv.setText(musicBean.getSong_name());
        holder.singerTv.setText(musicBean.getSinger_id());
        holder.timeTv.setText(musicBean.getTime());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MusicViewHolder extends RecyclerView.ViewHolder{
        //定义item_view
        TextView idTv,songTv,singerTv,timeTv;
        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.item_music_num);
            songTv = itemView.findViewById(R.id.item_music_song);
            singerTv = itemView.findViewById(R.id.item_music_singer);
            timeTv = itemView.findViewById(R.id.item_music_duration);
        }
    }
}
