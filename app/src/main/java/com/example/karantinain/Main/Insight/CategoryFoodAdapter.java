package com.example.karantinain.Main.Insight;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karantinain.Main.Home.RecommendActivityData;
import com.example.karantinain.R;

import java.util.ArrayList;

public class CategoryFoodAdapter extends RecyclerView.Adapter<CategoryFoodAdapter.CategoryFoodHolder> {
    private ArrayList<CategoryFood> categoryFoodArrayList;
    Context context;
    boolean isActive;

    public CategoryFoodAdapter(ArrayList<CategoryFood> list){
        this.categoryFoodArrayList = list;
    }

    @Override
    public int getItemViewType(int position) {
        if(categoryFoodArrayList.get(position).isActive()){
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public CategoryFoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insight_category_food_active, parent, false);
            context = view.getContext();
            return new CategoryFoodAdapter.CategoryFoodHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insight_category_food_inactive, parent, false);
            context = view.getContext();
            return new CategoryFoodAdapter.CategoryFoodHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryFoodHolder holder, int position) {

        holder.setCategoryFoodData(categoryFoodArrayList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InsightFragment insightFragment = new InsightFragment();
                Toast.makeText(context, categoryFoodArrayList.get(position).getCategory(), Toast.LENGTH_SHORT).show();
                
//                InsightUtils.setCategory(context, categoryFoodArrayList.get(position).getCategory());

//                if (!insightFragment.foodDataArrayList.isEmpty()){
//                    Log.d("TAG", "tertekan");
//                    (InsightFragment.filter(categoryFoodArrayList.get(position).getCategory());
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryFoodArrayList.size();
    }


    public class CategoryFoodHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public CategoryFoodHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvCategoryName);
        }

        void setCategoryFoodData(CategoryFood categoryFood){
            tvTitle.setText(categoryFood.getTitle());
        }
    }
}
