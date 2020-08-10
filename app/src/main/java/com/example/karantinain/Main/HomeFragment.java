package com.example.karantinain.Main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karantinain.R;
import com.example.karantinain.Utils.SharedPrefManager;

public class HomeFragment extends Fragment {
    TextView tvName;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvName = view.findViewById(R.id.tvName);
        tvName.setText("Hai, "+ SharedPrefManager.getFullNameProfile(getContext()));



        return view;
    }
}