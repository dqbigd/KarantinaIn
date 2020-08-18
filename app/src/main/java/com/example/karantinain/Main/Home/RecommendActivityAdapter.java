package com.example.karantinain.Main.Home;

import android.content.Context;
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

import java.util.ArrayList;

public class RecommendActivityAdapter extends RecyclerView.Adapter<RecommendActivityAdapter.RecommendActivityHolder>{
    private ArrayList<RecommendActivityData> listRecommendActivity;
    private Context context;

    public RecommendActivityAdapter(ArrayList<RecommendActivityData> list){
        this.listRecommendActivity = list;
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
        Glide.with(holder.itemView.getContext())
                .load(recommendActivityData.getImage())
                .into(holder.imgBanner);
        holder.imgBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
//        int limit = 3;
//
//        if (listRecommendActivity.size() > limit){
//            return limit;
//        }else {
//            return listRecommendActivity.size();
//        }

        return listRecommendActivity.size();
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
