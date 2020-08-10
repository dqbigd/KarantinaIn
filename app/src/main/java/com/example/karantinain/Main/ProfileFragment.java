package com.example.karantinain.Main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karantinain.Login.MenuLoginActivity;
import com.example.karantinain.R;
import com.example.karantinain.Utils.SharedPrefManager;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        view.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.setLoggedInStatus(getContext(), false, "");
                SharedPrefManager.setAccount(getContext(), "", "");
                startActivity(new Intent(getContext(), MenuLoginActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }
}