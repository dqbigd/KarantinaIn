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

import java.util.ArrayList;

public class ContentEducationAdapter extends RecyclerView.Adapter<ContentEducationAdapter.ContentEducationHolder>{
    private ArrayList<ContentEducationData> listContentEducation;
    private Context context;

    public ContentEducationAdapter(ArrayList<ContentEducationData> list){
        this.listContentEducation = list;
    }

    @NonNull
    @Override
    public ContentEducationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_content, parent, false);
        context = view.getContext();
        return new ContentEducationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentEducationHolder holder, int position) {
        ContentEducationData contentEducationData = listContentEducation.get(position);

        holder.tvTitle.setText(contentEducationData.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(contentEducationData.getImage())
                .into(holder.imgBanner);
        holder.imgBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sLink = contentEducationData.getLink();
                String sTitleBar = contentEducationData.getTitle();

                Intent intentDetail = new Intent(context, DetailKegiatanActivity.class);

                intentDetail.putExtra("EXTRA_DETAIL_KEGIATAN_TITLE", sTitleBar);
                intentDetail.putExtra("EXTRA_DETAIL_KEGIATAN_LINK", sLink);

                context.startActivity(intentDetail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listContentEducation.size();
    }

    public class ContentEducationHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgBanner;
        ImageButton imgBtnOpen;
        public ContentEducationHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            imgBtnOpen = itemView.findViewById(R.id.imgBtnOpen);
        }
    }
}
