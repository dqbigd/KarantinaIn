package com.example.karantinain.Login.ForgotPassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karantinain.R;
import com.jkb.vcedittext.VerificationCodeEditText;

public class VerifikasiFragment extends Fragment {
    Button btnNext;
    VerificationCodeEditText etVerificationCode;

    public static String EXTRA_EMAIL = "extra_email";

    public VerifikasiFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verifikasi, container, false);

        btnNext = view.findViewById(R.id.btnNext);
        etVerificationCode = view.findViewById(R.id.etVerificationCode);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificationCode = etVerificationCode.getText().toString();
                String email = getArguments().getString(EXTRA_EMAIL);

                PasswordFragment passwordFragment = new PasswordFragment();

                Bundle bundle = new Bundle();
                bundle.putString(PasswordFragment.EXTRA_EMAIL, email);
                bundle.putString(PasswordFragment.EXTRA_VERIFICATION_CODE, verificationCode);

                passwordFragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.flReset, passwordFragment, PasswordFragment.class.getSimpleName())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        return view;
    }
}