package com.example.karantinain.Main.Insight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

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

public class VideoActivity extends AppCompatActivity {
    ProgressBar pbVideo;
    RecyclerView rvVideo;

    private ArrayList videoDataArrayList = new ArrayList<>();
    private VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        pbVideo = findViewById(R.id.pbVideo);
        rvVideo = findViewById(R.id.rvVideo);

        findViewById(R.id.imgBtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupVideo();

    }

    private void setupVideo() {
        pbVideo.setVisibility(View.VISIBLE);

        rvVideo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvVideo.setAdapter(videoAdapter);

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(rvVideo);

        String token = SharedPrefManager.getKeyToken(getApplicationContext());

        Call<VideoResponse> call = InitRetrofit.getInstance().video(token);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful()) {
                    pbVideo.setVisibility(View.GONE);
                    if (response.body() != null && response.body().getMessage().equals("Ok.")) {
                        if (response.body().getData().toString().equals("[]")) {
                            Toast.makeText(getApplicationContext(), "Kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            rvVideo.setVisibility(View.VISIBLE);
                            videoDataArrayList = new ArrayList<>(response.body().getData());
                            videoAdapter = new VideoAdapter(videoDataArrayList, true);
                            rvVideo.setAdapter(videoAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Mohon cek jaringan internet anda", Toast.LENGTH_SHORT).show();
                Log.d("Response Error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}