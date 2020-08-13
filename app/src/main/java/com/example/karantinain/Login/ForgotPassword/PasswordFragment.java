package com.example.karantinain.Login.ForgotPassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.karantinain.R;

public class PasswordFragment extends Fragment {

    public static String EXTRA_EMAIL = "extra_email";
    public static String EXTRA_VERIFICATION_CODE = "extra_verification_code";

    public PasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        String email = getArguments().getString(EXTRA_EMAIL);
        String verificationCode = getArguments().getString(EXTRA_VERIFICATION_CODE);

        Toast.makeText(getContext(), email+" "+verificationCode, Toast.LENGTH_SHORT).show();

        return view;
    }
}