package com.example.karantinain.Main.Insight;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karantinain.Api.InitRetrofit;
import com.example.karantinain.R;
import com.example.karantinain.Utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsightFragment extends Fragment {
    ProgressBar pbFood;
    RecyclerView rvVideo, rvFoodCategory, rvFoodRecommendation, rvSport;

    private ArrayList<CategoryFood> categoryFoodArrayList = new ArrayList<>();
    private CategoryFoodAdapter categoryFoodAdapter;
    private ArrayList<FoodData> foodDataArrayList = new ArrayList<>();
    private FoodAdapter foodAdapter;
    
    public InsightFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insight, container, false);

        rvFoodCategory = view.findViewById(R.id.rvFoodCategory);
        rvFoodRecommendation = view.findViewById(R.id.rvFoodRecommendation);
        pbFood = view.findViewById(R.id.pbFood);

        view.findViewById(R.id.imgBtnPahami).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentDetail = new Intent(getContext(), DetailArticleInsightActivity.class);

                intentDetail.putExtra("EXTRA_DETAIL_INSIGHT_ARTICLE_TITLE", "10 tanda daya tahan tubuh anda melemah");
                intentDetail.putExtra("EXTRA_DETAIL_INSIGHT_ARTICLE_LINK", "https://www.cnnindonesia.com/gaya-hidup/20200220094602-255-476375/10-tanda-daya-tahan-tubuh-lemah");

                startActivity(intentDetail);
            }
        });

        setupCategoryFood();
        setupFood();



        return view;
    }

    private void setupCategoryFood() {
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(rvFoodRecommendation);

        CategoryFood item1 = new CategoryFood();
        item1.setTitle("All");
        item1.setActive(true);
        item1.setCategory("all");

        CategoryFood item2 = new CategoryFood();
        item2.setTitle("Buah - buahan");
        item2.setActive(false);
        item2.setCategory("buah");

        CategoryFood item3 = new CategoryFood();
        item3.setTitle("Sayur");
        item3.setActive(false);
        item3.setCategory("sayur");

        CategoryFood item4 = new CategoryFood();
        item4.setTitle("Daging");
        item4.setActive(false);
        item4.setCategory("daging");

        categoryFoodArrayList.add(item1);
        categoryFoodArrayList.add(item2);
        categoryFoodArrayList.add(item3);
        categoryFoodArrayList.add(item4);

        categoryFoodAdapter = new CategoryFoodAdapter(categoryFoodArrayList);

        rvFoodCategory.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFoodCategory.setAdapter(categoryFoodAdapter);
    }

    public void filter(String category) {
        ArrayList<FoodData> filteredList = new ArrayList<>();
        if (!category.equals("all")){
            for (FoodData item : foodDataArrayList){
                if (item.getCategory().toLowerCase().equals(category.toLowerCase())){
                    filteredList.add(item);
                }
            }
        }

        foodAdapter.filterList(filteredList);
    }

    public void setupFood() {
        rvFoodRecommendation.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFoodRecommendation.setAdapter(foodAdapter);

        pbFood.setVisibility(View.VISIBLE);
        String token = SharedPrefManager.getKeyToken(getContext());

        Call<FoodResponse> call = InitRetrofit.getInstance().food(token);
        call.enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {
                if (response.isSuccessful()) {
                    pbFood.setVisibility(View.GONE);
                    if (response.body() != null && response.body().getMessage().equals("Ok.")) {
                        if (response.body().getData().toString().equals("[]")) {
                            Toast.makeText(getContext(), "Kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            rvFoodRecommendation.setVisibility(View.VISIBLE);
                            foodDataArrayList = new ArrayList<>(response.body().getData());
                            foodAdapter = new FoodAdapter(foodDataArrayList);
                            rvFoodRecommendation.setAdapter(foodAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FoodResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Mohon cek jaringan internet anda", Toast.LENGTH_SHORT).show();
                Log.d("Response Error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}