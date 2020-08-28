package com.example.karantinain.Main.Insight;

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

import java.util.ArrayList;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodHolder>{
    private ArrayList<FoodData> foodDataArrayList;
    private Context context;

    public FoodAdapter(ArrayList<FoodData> list){
        this.foodDataArrayList = list;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insight_food, parent, false);
        context = view.getContext();
        return new FoodAdapter.FoodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        FoodData foodData = foodDataArrayList.get(position);

        holder.tvTitle.setText(foodData.getTitle());
        holder.tvDesc.setText(foodData.getDescription());
        Glide.with(holder.itemView.getContext())
                .load(foodData.getImage())
                .into(holder.imgBanner);
        holder.imgBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sLink = foodData.getLink();
                String sTitleBar = foodData.getTitle();

                Intent intentDetail = new Intent(context, DetailArticleInsightActivity.class);

                intentDetail.putExtra("EXTRA_DETAIL_INSIGHT_ARTICLE_TITLE", sTitleBar);
                intentDetail.putExtra("EXTRA_DETAIL_INSIGHT_ARTICLE_LINK", sLink);

                context.startActivity(intentDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodDataArrayList.size();
    }

    public void filterList(ArrayList<FoodData> filteredList) {
        foodDataArrayList = filteredList;
        notifyDataSetChanged();
    }

    public class FoodHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        ImageView imgBanner;
        ImageButton imgBtnOpen;
        public FoodHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            imgBtnOpen = itemView.findViewById(R.id.imgBtnOpen);
        }
    }
}
