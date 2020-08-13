package com.example.karantinain.Login.ForgotPassword;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karantinain.Api.InitRetrofit;
import com.example.karantinain.R;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordFragment extends Fragment {
    EditText etPassword, etPasswordConfirm;
    Button btnChangePass;

    public static String EXTRA_EMAIL = "extra_email";
    public static String EXTRA_VERIFICATION_CODE = "extra_verification_code";
    Boolean validation;

    public PasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);

        etPassword = view.findViewById(R.id.etPassword);
        etPasswordConfirm = view.findViewById(R.id.etPasswordConfirm);
        btnChangePass = view.findViewById(R.id.btnChangePass);

        etPasswordConfirm.addTextChangedListener(confirmWatcher);

        String email = getArguments().getString(EXTRA_EMAIL);
        String verificationCode = getArguments().getString(EXTRA_VERIFICATION_CODE);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = etPassword.getText().toString();
                String passCon = etPasswordConfirm.getText().toString();

                if (TextUtils.isEmpty(pass)){
                    etPassword.setError("Field tidak boleh kosong");
                    validation = false;
                }else if(TextUtils.isEmpty(passCon)){
                    etPasswordConfirm.setError("Field tidak boleh kosong");
                    validation = false;
                }

                if (validation){
                    sendData(email, verificationCode, pass);
                }

            }
        });

        return view;
    }

    private void sendData(String email, String verificationCode, String password) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Mengirimkan data");
        dialog.setMessage("Loading ...");
        dialog.setCancelable(true);
        dialog.show();

        Call<ForgotPasswordResponse> call = InitRetrofit.getInstance().passwordChange(email, verificationCode, password);
        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equals("Ok.")){
                        dialog.dismiss();
                        getActivity().finish();
                        Toast.makeText(getContext(), "Berhasil, silahkan login kembali", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Gagal, mohon coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Mohon cek koneksi anda ", Toast.LENGTH_SHORT).show();
                Log.d("onFailurePasswordReset", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private TextWatcher confirmWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etPassword.length() == etPasswordConfirm.length() || etPassword.length() < etPasswordConfirm.length()){
                if (!etPassword.getText().toString().equals(etPasswordConfirm.getText().toString())){
                    etPasswordConfirm.setError("Konfirmasi Password harus sama");
                    validation = false;
                }else{
                    validation = true;
                }
            }else{
                validation = false;
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}