package com.example.karantinain.Main.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class KegiatanActivity extends AppCompatActivity {
    RecyclerView rvKegiatan;
    ProgressBar pbKegiatan;

    private ArrayList<RecommendActivityData> recommendActivityDataArrayList = new ArrayList<>();
    private RecommendActivityAdapter recommendActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kegiatan);

        rvKegiatan = findViewById(R.id.rvKegiatan);
        pbKegiatan = findViewById(R.id.pbKegiatan);

        findViewById(R.id.imgBtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupRecommendActivity();

    }

    private void setupRecommendActivity() {
        rvKegiatan.setLayoutManager(new LinearLayoutManager(KegiatanActivity.this));
        rvKegiatan.setAdapter(recommendActivityAdapter);

        pbKegiatan.setVisibility(View.VISIBLE);
        String token = SharedPrefManager.getKeyToken(KegiatanActivity.this);

        Call<RecommendActivityResponse> call = InitRetrofit.getInstance().recommendActivity(token);
        call.enqueue(new Callback<RecommendActivityResponse>() {
            @Override
            public void onResponse(Call<RecommendActivityResponse> call, Response<RecommendActivityResponse> response) {
                if (response.isSuccessful()) {
                    pbKegiatan.setVisibility(View.GONE);
                    if (response.body() != null && response.body().getMessage().equals("Ok.")) {
                        if (response.body().getData().toString().equals("[]")) {
                            Toast.makeText(KegiatanActivity.this, "Kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            rvKegiatan.setVisibility(View.VISIBLE);
                            recommendActivityDataArrayList = new ArrayList<>(response.body().getData());
                            recommendActivityAdapter = new RecommendActivityAdapter(recommendActivityDataArrayList,false, 0);
                            rvKegiatan.setAdapter(recommendActivityAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RecommendActivityResponse> call, Throwable t) {
                Toast.makeText(KegiatanActivity.this, "Mohon cek jaringan internet anda", Toast.LENGTH_SHORT).show();
                Log.d("Response Error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

}