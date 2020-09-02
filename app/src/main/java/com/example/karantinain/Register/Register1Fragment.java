package com.example.karantinain.Register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.karantinain.Login.ForgotPassword.VerifikasiFragment;
import com.example.karantinain.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        Log.d("TAG", "ini"+RegisterUtils.getKeyRegisterLatitude(getContext())+"ini");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validation = true;

                String name = etFullName.getText().toString();
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String phone = etPhoneNumber.getText().toString();

                if(TextUtils.isEmpty(name)){
                    etFullName.setError("Field tidak boleh kosong");
                    validation = false;
                }else if (!isValidEmail(email)) {
                    etEmail.setError("Email tidak valid");
                    validation = false;
                }else if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Field tidak boleh kosong");
                    validation = false;
                }else if (TextUtils.isEmpty(username)) {
                    etUsername.setError("Field tidak boleh kosong");
                    validation = false;
                }else if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Field tidak boleh kosong");
                    validation = false;
                }else if (password.length() < 8) {
                    etPassword.setError("Password minimal memiliki 8 karakter");
                    validation = false;
                }else if (!isValidPassword(password)) {
                    etPassword.setError("Password harus memiliki huruf kapital, simbol, angka");
                    validation = false;
                }else if (TextUtils.isEmpty(phone)) {
                    etPhoneNumber.setError("Field tidak boleh kosong");
                    validation = false;
                }


                if (validation){
                    nextFragment(name, email, username, password, phone);
                }
            }
        });



        return view;
    }

    private void nextFragment(String name, String email, String username, String password, String phone) {
        Register2Fragment register2Fragment = new Register2Fragment();

        Bundle bundle = new Bundle();
        bundle.putString(Register2Fragment.EXTRA_NAME, name);
        bundle.putString(Register2Fragment.EXTRA_EMAIL, email);
        bundle.putString(Register2Fragment.EXTRA_USERNAME, username);
        bundle.putString(Register2Fragment.EXTRA_PASSWORD, password);
        bundle.putString(Register2Fragment.EXTRA_PHONE, phone);

        register2Fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.flRegister, register2Fragment, Register2Fragment.class.getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){8,16}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RegisterUtils.setRegisterLocation(getContext(), 0, 0);
    }
}