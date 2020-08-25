package com.example.karantinain.Main.Insight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.karantinain.R;

public class DetailArticleInsightActivity extends AppCompatActivity {
    TextView tvTitleBar;
    WebView wvDetailInsight;
    ProgressBar pbInsight;

    String sTitleBar, sLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article_insight);

        wvDetailInsight = findViewById(R.id.wvDetailInsight);
        tvTitleBar = findViewById(R.id.tvTitleBar);
        pbInsight = findViewById(R.id.pbInsight);

        findViewById(R.id.imgBtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sTitleBar = getIntent().getStringExtra("EXTRA_DETAIL_INSIGHT_ARTICLE_TITLE");
        sLink = getIntent().getStringExtra("EXTRA_DETAIL_INSIGHT_ARTICLE_LINK");

        wvDetailInsight.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                tvTitleBar.setText("Loading...");
                pbInsight.setProgress(progress); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress == 100)
                    pbInsight.setVisibility(View.GONE);
                tvTitleBar.setText(sTitleBar);
            }
        });
        wvDetailInsight.setWebViewClient(new WebViewClient());
//        wvDetailKegiatan.getSettings().setJavaScriptEnabled(true);
        wvDetailInsight.loadUrl(sLink);
    }
}