package com.example.karantinain.Register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.karantinain.R;

public class Register1Fragment extends Fragment {
    private Button btnNext;
    private EditText etFullName, etEmail, etUsername, etPassword, etPhoneNumber;

    public Register1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register1, container, false);

        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        btnNext = view.findViewById(R.id.btnNext);

        view.findViewById(R.id.lyTwo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity)getActivity()).selectFragment(1);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity)getActivity()).selectFragment(1);
            }
        });



        return view;
    }
}