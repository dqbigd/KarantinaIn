package com.example.karantinain.Login.ForgotPassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karantinain.R;

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
        });

        return view;
    }

}