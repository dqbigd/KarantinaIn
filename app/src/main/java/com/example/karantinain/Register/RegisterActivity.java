package com.example.karantinain.Register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.karantinain.R;

public class RegisterActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterPagerAdapter registerPagerAdapter= new RegisterPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPager = findViewById(R.id.vpRegister);
        viewPager.setAdapter(registerPagerAdapter);

        findViewById(R.id.imgBtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void selectFragment(int position){
        viewPager.setCurrentItem(position, true);
    }
}