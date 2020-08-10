package com.example.karantinain.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karantinain.Api.InitRetrofit;
import com.example.karantinain.Main.MainActivity;
import com.example.karantinain.Register.RegisterActivity;
import com.example.karantinain.R;
import com.example.karantinain.Utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });

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

    private void sendData() {
        final String sUsername = etUsername.getText().toString();
        final String sPassword = etPassword.getText().toString();

        Call<LoginResponse> call = InitRetrofit.getInstance().signIn(sUsername, sPassword);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equals("Ok.")){
                        SharedPrefManager.setAccount(getBaseContext(), sUsername, sPassword);
                        String token = response.body().getData();

                        SharedPrefManager.setLoggedInStatus(getBaseContext(),true, token);

                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                        finish();
                    }else if(response.body().getMessage().equals("Not found.")){
                        Toast.makeText(LoginActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                    }else if(response.body().getMessage().equals("Invalid request!")){
                        Toast.makeText(LoginActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Mohon cek username atau password anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Mohon daftar / cek koneksi anda dahulu :D", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });
    }
}