package com.example.karantinain.Main.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.karantinain.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecommendActivityAdapter extends RecyclerView.Adapter<RecommendActivityAdapter.RecommendActivityHolder>{
    private ArrayList<RecommendActivityData> listRecommendActivity;
    private Context context;
    int limit;
    boolean bLimit;

    public RecommendActivityAdapter(ArrayList<RecommendActivityData> list, boolean bLimit , Integer limit){
        this.listRecommendActivity = list;
        this.limit = limit;
        this.bLimit = bLimit;
    }

    @NonNull
    @Override
    public RecommendActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_kegiatan, parent, false);
        context = view.getContext();
        return new RecommendActivityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendActivityHolder holder, int position) {
        RecommendActivityData recommendActivityData = listRecommendActivity.get(position);

        holder.tvTitle.setText(recommendActivityData.getTitle());
//        Picasso.get().load("https://equal.lug-surabaya.com/images/jobs/1583470828.png").into(holder.imgBanner);
        Glide.with(holder.itemView.getContext())
                .load("http://"+recommendActivityData.getImage())
                .into(holder.imgBanner);
        holder.imgBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sLink = recommendActivityData.getLink();
                String sTitleBar = recommendActivityData.getTitle();

                Intent intentDetail = new Intent(context, DetailKegiatanActivity.class);

                intentDetail.putExtra("EXTRA_DETAIL_KEGIATAN_TITLE", sTitleBar);
                intentDetail.putExtra("EXTRA_DETAIL_KEGIATAN_LINK", sLink);

                context.startActivity(intentDetail);
            }
        });

    }

    @Override
    public int getItemCount() {

        if (bLimit){
            if (listRecommendActivity.size() > limit){
                return limit;
            }else {
                return listRecommendActivity.size();
            }
        }else{
            return listRecommendActivity.size();
        }

    }

    public class RecommendActivityHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgBanner;
        ImageButton imgBtnOpen;
        public RecommendActivityHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            imgBtnOpen = itemView.findViewById(R.id.imgBtnOpen);
        }
    }
}
