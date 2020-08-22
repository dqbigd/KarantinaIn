package com.example.karantinain.Register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.karantinain.Login.ForgotPassword.EmailFragment;
import com.example.karantinain.R;

public class RegisterActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        RegisterPagerAdapter registerPagerAdapter= new RegisterPagerAdapter(getSupportFragmentManager(), getApplicationContext());
//        viewPager = findViewById(R.id.vpRegister);
//        viewPager.setAdapter(registerPagerAdapter);

        findViewById(R.id.imgBtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        Register1Fragment register1Fragment = new Register1Fragment();
        Fragment fragment = fragmentManager.findFragmentByTag(Register1Fragment.class.getSimpleName());

        if (!(fragment instanceof Register1Fragment)){
            fragmentManager
                    .beginTransaction()
                    .add(R.id.flRegister, register1Fragment, Register1Fragment.class.getSimpleName())
                    .commit();
        }

    }
//    public void selectFragment(int position){
//        viewPager.setCurrentItem(position, true);
//    }
}