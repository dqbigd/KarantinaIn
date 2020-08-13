package com.example.karantinain.Login.ForgotPassword;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karantinain.Api.InitRetrofit;
import com.example.karantinain.Login.LoginActivity;
import com.example.karantinain.Login.LoginResponse;
import com.example.karantinain.R;
import com.example.karantinain.Utils.SharedPrefManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailFragment extends Fragment{
    EditText etEmail;
    Button btnNext;

    public EmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email, container, false);

        etEmail = view.findViewById(R.id.etEmail);
        btnNext = (Button) view.findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                Boolean validation = true;

                if (!isValidEmail(email)) {
                    etEmail.setError("Email tidak valid");
                    validation = false;
                }

                if (validation){
                    sendData(email);
                }
            }
        });

        return view;
    }

    private void sendData(String email) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Mengirimkan data");
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);
        dialog.show();

        Call<ForgotPasswordResponse> call = InitRetrofit.getInstance().sendEmail(email);
        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equals("Ok.")){
                        dialog.hide();
                        nextFragment(email);
                    }
                }else {
                    Toast.makeText(getContext(), "Gagal, mohon coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Mohon cek koneksi anda ", Toast.LENGTH_SHORT).show();
                Log.d("onFailureSendEmail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void nextFragment(String email) {
        VerifikasiFragment verifikasiFragment = new VerifikasiFragment();

        Bundle bundle = new Bundle();
        bundle.putString(VerifikasiFragment.EXTRA_EMAIL, email);

        verifikasiFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.flReset, verifikasiFragment, VerifikasiFragment.class.getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}