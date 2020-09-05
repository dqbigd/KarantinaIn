package com.example.karantinain.Main.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.karantinain.R;
import com.example.karantinain.Utils.SharedPrefManager;

public class EditAccountActivity extends AppCompatActivity {
    private EditText etFullName, etPhoneNumber, etUsername;
    private Spinner spGender, spAge;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        etFullName = findViewById(R.id.etFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etUsername = findViewById(R.id.etUsername);
        spGender = findViewById(R.id.spGender);
        spAge = findViewById(R.id.spAge);
        btnNext = findViewById(R.id.btnNext);

        setupProfileData();

        findViewById(R.id.imgBtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupProfileData() {
        etFullName.setText(SharedPrefManager.getFullNameProfile(getApplicationContext()));
        etUsername.setText(SharedPrefManager.getUserNameProfile(getApplicationContext()));
        etPhoneNumber.setText(SharedPrefManager.getPhoneProfile(getApplicationContext()));
    }
}