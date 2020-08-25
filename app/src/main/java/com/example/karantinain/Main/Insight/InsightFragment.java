package com.example.karantinain.Main.Insight;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.karantinain.Main.Home.DetailKegiatanActivity;
import com.example.karantinain.R;

public class InsightFragment extends Fragment {
    ImageButton imgBtnPahami;
    
    public InsightFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insight, container, false);

        view.findViewById(R.id.imgBtnPahami).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentDetail = new Intent(getContext(), DetailArticleInsightActivity.class);

                intentDetail.putExtra("EXTRA_DETAIL_INSIGHT_ARTICLE_TITLE", "10 tanda daya tahan tubuh anda melemah");
                intentDetail.putExtra("EXTRA_DETAIL_INSIGHT_ARTICLE_LINK", "https://www.cnnindonesia.com/gaya-hidup/20200220094602-255-476375/10-tanda-daya-tahan-tubuh-lemah");

                startActivity(intentDetail);
            }
        });

        return view;
    }
}