package com.example.karantinain.Main.Insight;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karantinain.R;

import java.util.ArrayList;

public class CategoryFoodAdapter extends RecyclerView.Adapter<CategoryFoodAdapter.CategoryFoodHolder> {
    private ArrayList<CategoryFood> categoryFoodArrayList;

    public CategoryFoodAdapter(ArrayList<CategoryFood> list){
        this.categoryFoodArrayList = list;
    }

    @NonNull
    @Override
    public CategoryFoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insight_category_food_active, parent, false);
        return new CategoryFoodAdapter.CategoryFoodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryFoodHolder holder, int position) {
        holder.setCategoryFoodData(categoryFoodArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryFoodArrayList.size();
    }


    public class CategoryFoodHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public CategoryFoodHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        void setCategoryFoodData(CategoryFood categoryFood){
            tvTitle.setText(categoryFood.getTitle());
        }
    }
}
