package com.example.karantinain.Login.ForgotPassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.karantinain.R;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        findViewById(R.id.imgBtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        EmailFragment emailFragment = new EmailFragment();
        Fragment fragment = fragmentManager.findFragmentByTag(EmailFragment.class.getSimpleName());

        if (!(fragment instanceof EmailFragment)){
            fragmentManager
                    .beginTransaction()
                    .add(R.id.flReset, emailFragment, EmailFragment.class.getSimpleName())
                    .commit();
        }
    }
}