package com.example.karantinain.Onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.karantinain.MenuLoginActivity;
import com.example.karantinain.R;

import java.util.ArrayList;

public class OnboardingActivity extends AppCompatActivity {
    private OnboardingAdapter onboardingAdapter;
    private ViewPager2 vpOnboarding;
    private LinearLayout lyIndicator;
    private Button btnNext;
    private TextView btnPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        lyIndicator = findViewById(R.id.lyOnBoardingIndicators);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);

        btnPrevious.setVisibility(View.VISIBLE);
        setupOnboardingItems();

        vpOnboarding = findViewById(R.id.vpOnboarding);
        vpOnboarding.setAdapter(onboardingAdapter);

        setupOnboardingIndicators();
        setCurrentIndicator(0);

        vpOnboarding.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vpOnboarding.getCurrentItem() + 1 < onboardingAdapter.getItemCount()){
                    vpOnboarding.setCurrentItem(vpOnboarding.getCurrentItem() + 1);
                }else{
                    Intent intent = new Intent(getApplicationContext(), MenuLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vpOnboarding.getCurrentItem() > 0) {
                    vpOnboarding.setCurrentItem(vpOnboarding.getCurrentItem() - 1);
                }
            }
        });
    }

    private void setCurrentIndicator(int index) {
        int childCount = lyIndicator.getChildCount();
        for (int i = 0;i < childCount; i++){
            ImageView imageView = (ImageView)lyIndicator.getChildAt(i);
            if (i == index){
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_onboarding_indicator_active));
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_onboarding_indicator_inactive));
            }
        }
        if (index == onboardingAdapter.getItemCount() - 1 ){
            btnNext.setText("Mulai");
        }else {
            btnNext.setText("Lanjut");
        }

        if(index > 0){
            btnPrevious.setVisibility(View.VISIBLE);
        }else{
            btnPrevious.setVisibility(View.GONE);
        }
    }

    private void setupOnboardingIndicators() {
        ImageView [] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,0,10,0);
        for (int i = 0;i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_onboarding_indicator_inactive));

            indicators[i].setLayoutParams(layoutParams);
            lyIndicator.addView(indicators[i]);
        }
    }

    private void setupOnboardingItems() {
        ArrayList<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem item1 = new OnboardingItem();
        item1.setTitle("Membantu gugus tugas COVID-19 untuk mengontrol perkembangan ODP dalam masa karantina mandiri");
        item1.setDescription("Memonitoring perkembangan pasien ODP dengan mengetahui kondisi immunitas tubuh pasien dan memastikan pasien tetap berada dirumah selama masa karinta mandiri.");
        item1.setImage(R.drawable.pic_onboard1);

        OnboardingItem item2 = new OnboardingItem();
        item2.setTitle("Selfie wajah dan membagikan lokasi saat ini guna mempermudah gugus tugas COVID-19 dalam memonitoring");
        item2.setDescription("Pasien ODP diharuskan mengirim foto wajah beserta sekitar rumahnya, kemudian dilanjutkan dengan membagikan lokasi saat ini, untuk mempermudah gugus tugas COVID-19 dalam memonitoring karantina mandiri");
        item2.setImage(R.drawable.pic_onboard2);

        OnboardingItem item3 = new OnboardingItem();
        item3.setTitle("Immunitas tubuh pasien ODP dan OTG semakin meningkat dengan Informasi makanan bergizi");
        item3.setDescription("Anjuran dan rekomendasi makanan - makanan bergizi untuk menjaga dan meningkatkan immunitas tubuh yang direkomendasikan oleh gugus tugas COVID-19 dengan menyeseuaikan kondisi tubuh dan gejala - gejala yang dialami si ODP");
        item3.setImage(R.drawable.pic_onboard3);

        OnboardingItem item4 = new OnboardingItem();
        item4.setTitle("Immunitas tubuh pasien ODP dan OTG semakin meningkat dengan Informasi olahraga yang efektif");
        item4.setDescription("Anjuran dan rekomendasi olahraga yang efektih untuk menjaga dan meningkatkan immunitas tubuh yang direkomendasikan oleh gugus tugas COVID-19 dengan menyeseuaikan kondisi tubuh dan gejala - gejala yang dialami si ODP");
        item4.setImage(R.drawable.pic_onboard4);

        onboardingItems.add(item1);
        onboardingItems.add(item2);
        onboardingItems.add(item3);
        onboardingItems.add(item4);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }


}