package com.example.karantinain.Main.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.karantinain.R;

public class DetailKegiatanActivity extends AppCompatActivity {
    TextView tvTitleBar;
    WebView wvDetailKegiatan;
    ProgressBar pbKegiatan;

    String sTitleBar, sLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kegiatan);

        wvDetailKegiatan = findViewById(R.id.wvDetailKegiatan);
        tvTitleBar = findViewById(R.id.tvTitleBar);
        pbKegiatan = findViewById(R.id.pbKegiatan);

        findViewById(R.id.imgBtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sTitleBar = getIntent().getStringExtra("EXTRA_DETAIL_KEGIATAN_TITLE");
        sLink = getIntent().getStringExtra("EXTRA_DETAIL_KEGIATAN_LINK");

        wvDetailKegiatan.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                tvTitleBar.setText("Loading...");
                pbKegiatan.setProgress(progress); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress == 100)
                    pbKegiatan.setVisibility(View.GONE);
                    tvTitleBar.setText(sTitleBar);
            }
        });
        wvDetailKegiatan.setWebViewClient(new WebViewClient());
        wvDetailKegiatan.loadUrl(sLink);
    }
}