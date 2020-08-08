package com.example.karantinain.Onboarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karantinain.R;

import java.util.ArrayList;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingHolder> {
    private ArrayList<OnboardingItem> listOnboarding;

    public OnboardingAdapter(ArrayList<OnboardingItem> list){
        this.listOnboarding = list;
    }

    @NonNull
    @Override
    public OnboardingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false);
//        context = view.getContext();
        return new OnboardingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listOnboarding.size();
    }

    public class OnboardingHolder extends RecyclerView.ViewHolder {
        private ImageView imgOnboarding;
        private TextView tvTitle;
        private TextView tvDesc;

        public OnboardingHolder(@NonNull View itemView) {
            super(itemView);

            imgOnboarding = itemView.findViewById(R.id.imgOnboarding);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }
}
