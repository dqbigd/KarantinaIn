package com.example.karantinain.Main.Insight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.karantinain.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoDetailActivity extends AppCompatActivity {
    private TextView tvTitle;
    private YouTubePlayerView youtube_player_view;
    private String sTitleBar, sLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        tvTitle = findViewById(R.id.tvTitle);
        youtube_player_view = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youtube_player_view);

        findViewById(R.id.imgBtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sTitleBar = getIntent().getStringExtra("EXTRA_DETAIL_VIDEO_TITLE");
        sLink = getIntent().getStringExtra("EXTRA_DETAIL_VIDEO_LINK");

        tvTitle.setText(sTitleBar);
        youtube_player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.loadVideo(sLink, 0);
            }
        });

    }
}