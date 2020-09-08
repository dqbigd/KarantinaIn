package com.example.karantinain.Main.Profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.karantinain.Login.MenuLoginActivity;
import com.example.karantinain.R;
import com.example.karantinain.Utils.SharedPrefManager;

public class ProfileFragment extends Fragment {
    ImageView imgAccount;
    TextView tvName, tvAddress, tvIndication;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgAccount = view.findViewById(R.id.imgAccount);
        tvName = view.findViewById(R.id.tvName);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvIndication = view.findViewById(R.id.tvIndication);

        setupProfileData();

        view.findViewById(R.id.btnChooseFood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btnChooseSport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.tvFoodHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FoodHistoryActivity.class));
            }
        });

        view.findViewById(R.id.tvSportHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SportHistoryActivity.class));
            }
        });

        view.findViewById(R.id.lyEditAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditAccountActivity.class));
            }
        });

        view.findViewById(R.id.imgBtnEditAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditAccountActivity.class));
            }
        });

        view.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogLogout();

            }
        });

        return view;
    }

    private void setupProfileData() {
        String sGender = SharedPrefManager.getGenderProfile(getContext());
        if (sGender.equalsIgnoreCase("Laki - laki") || sGender.equalsIgnoreCase("Laki laki")){
            Glide.with(getContext())
                    .load(R.drawable.pic_profile_male)
                    .into(imgAccount);
        }else {
            Glide.with(getContext())
                    .load(R.drawable.pic_profile_female)
                    .into(imgAccount);
        }

        tvName.setText(SharedPrefManager.getFullNameProfile(getContext())+" ("+SharedPrefManager.getUserNameProfile(getContext())+")");
        tvAddress.setText("-");
        tvIndication.setText(SharedPrefManager.getIndicationProfile(getContext()));
    }

    private void openDialogLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Konfirmasi")
                .setMessage("Apakah kamu yakin ingin menonaktifkan lokasi saat ini?")
                .setNegativeButton("batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPrefManager.setLoggedInStatus(getContext(), false, "");
                SharedPrefManager.setAccount(getContext(), "", "");
                SharedPrefManager.setProfile(getContext(), "","", "", "", "", "", "");
                startActivity(new Intent(getContext(), MenuLoginActivity.class));
                getActivity().finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}