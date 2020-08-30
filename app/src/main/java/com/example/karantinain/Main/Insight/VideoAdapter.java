package com.example.karantinain.Main.Insight;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.karantinain.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder>{
    private ArrayList<VideoData> videoDataArrayList;
    private Context context;

    public VideoAdapter(ArrayList<VideoData> list){
        this.videoDataArrayList = list;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insight_video, parent, false);
        context = view.getContext();
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        String videoId;
        VideoData videoData = videoDataArrayList.get(position);

        holder.tvTitle.setText(videoData.getTitle());
//        videoId = videoData.getUrl();
//        String substring = videoId.substring(videoId.length() - 11);
//        Log.d("SUBSTRING", videoId);
//        holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(YouTubePlayer youTubePlayer) {
//                super.onReady(youTubePlayer);
//                youTubePlayer.cueVideo("8MTcml-rxgM", 0);
//            }
//        });
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
