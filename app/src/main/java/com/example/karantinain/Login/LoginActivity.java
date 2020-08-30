package com.example.karantinain.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.karantinain.Api.InitRetrofit;
import com.example.karantinain.Login.ForgotPassword.ResetPasswordActivity;
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

        findViewById(R.id.tvForgotPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
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
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle("Mengirimkan data");
        dialog.setMessage("Loading ...");
        dialog.setCancelable(true);
        dialog.show();

        final String sUsername = etUsername.getText().toString();
        final String sPassword = etPassword.getText().toString();

        Call<LoginResponse> call = InitRetrofit.getInstance().signIn(sUsername, sPassword);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equals("Ok.")){
                        dialog.dismiss();

                        SharedPrefManager.setAccount(getBaseContext(), sUsername, sPassword);
                        String token = response.body().getData();

                        SharedPrefManager.setLoggedInStatus(getBaseContext(),true, token);
                        loadData(token);
                    }else if(response.body().getMessage().equals("Not found.")){
                        Toast.makeText(LoginActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Mohon cek username atau password anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Mohon daftar / cek koneksi anda dahulu", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });
    }

    private void loadData(String token) {
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle("Mengambil data");
        dialog.setMessage("Loading ...");
        dialog.setCancelable(true);
        dialog.show();

        Call<ProfileResponse> call = InitRetrofit.getInstance().profile(token);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();

                    String id = response.body().getData().getId();
                    String username = response.body().getData().getUsername();
                    String fullname = response.body().getData().getFullname();
                    String phone = response.body().getData().getPhoneNumber();
                    String gender = response.body().getData().getGender();
                    String age = response.body().getData().getAge();
                    String indication = response.body().getData().getIndication();

                    Log.d("Success LoadData ", response.body().getData().getFullname());
                    SharedPrefManager.setProfile(getApplicationContext(), id, username, fullname, phone, gender, age, indication);
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this , t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}