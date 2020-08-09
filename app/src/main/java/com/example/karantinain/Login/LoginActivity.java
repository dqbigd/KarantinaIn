package com.example.karantinain.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.karantinain.Register.RegisterActivity;
import com.example.karantinain.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;

    final int sdk = android.os.Build.VERSION.SDK_INT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        findViewById(R.id.tvRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });

        etUsername.addTextChangedListener(loginTextWatcher);
        etPassword.addTextChangedListener(loginTextWatcher);

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String nim = etUsername.getText().toString().trim();
            String pin = etPassword.getText().toString().trim();

            if (!nim.isEmpty() && !pin.isEmpty()){
                btnLogin.setEnabled(true);
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnLogin.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_green_rounded) );
                } else {
                    btnLogin.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_green_rounded));
                }
            }else {
                btnLogin.setEnabled(false);
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnLogin.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_grey_rounded) );
                } else {
                    btnLogin.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_grey_rounded));
                }
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}