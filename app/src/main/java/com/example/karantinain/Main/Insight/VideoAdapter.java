package com.example.karantinain.Main.Insight;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.karantinain.R;

import java.util.ArrayList;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder>{
    private ArrayList<VideoData> videoDataArrayList;
    private Context context;
    private boolean vertical;

    public VideoAdapter(ArrayList<VideoData> list, boolean vertical){
        this.videoDataArrayList = list;
        this.vertical = vertical;
    }

    @Override
    public int getItemViewType(int position) {
        if(vertical){
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insight_video_detail, parent, false);
            context = view.getContext();
            return new VideoHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insight_video, parent, false);
            context = view.getContext();
            return new VideoHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        String videoId;
        VideoData videoData = videoDataArrayList.get(position);

        holder.tvTitle.setText(videoData.getTitle());
        videoId = videoData.getUrl().trim();
        String substring = videoId.substring(videoId.length() - 11);
//        Log.d("SUBSTRING", videoId);
//        holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(YouTubePlayer youTubePlayer) {
//                super.onReady(youTubePlayer);
//                youTubePlayer.cueVideo("8MTcml-rxgM", 0);
//            }
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sLink = substring;
                String sTitleBar = videoData.getTitle();

                Intent intentDetail = new Intent(context, VideoDetailActivity.class);

                intentDetail.putExtra("EXTRA_DETAIL_VIDEO_TITLE", sTitleBar);
                intentDetail.putExtra("EXTRA_DETAIL_VIDEO_LINK", sLink);

                context.startActivity(intentDetail);
            }
        });
        Glide.with(holder.itemView.getContext())
                .load(videoData.getImage())
                .into(holder.imgBanner);
//        holder.imgBtnOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String sLink = foodData.getLink();
//                String sTitleBar = foodData.getTitle();
//
//                Intent intentDetail = new Intent(context, DetailArticleInsightActivity.class);
//
//                intentDetail.putExtra("EXTRA_DETAIL_INSIGHT_ARTICLE_TITLE", sTitleBar);
//                intentDetail.putExtra("EXTRA_DETAIL_INSIGHT_ARTICLE_LINK", sLink);
//
//                context.startActivity(intentDetail);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return videoDataArrayList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgBanner;
        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
