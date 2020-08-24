package com.example.karantinain.Register;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.karantinain.BuildConfig;
import com.example.karantinain.Main.MainActivity;
import com.example.karantinain.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.material.snackbar.Snackbar;

public class Register2Fragment extends Fragment {
    private final static int PLACE_PICKER_REQUEST = 32;

    public static String EXTRA_NAME = "extra_name";
    public static String EXTRA_EMAIL = "extra_email";
    public static String EXTRA_USERNAME = "extra_username";
    public static String EXTRA_PASSWORD = "extra_password";
    public static String EXTRA_PHONE = "extra_phone";
    public static String EXTRA_LATITUDE = "extra_latitude";
    public static String EXTRA_LONGITUDE = "extra_longitude";

    WifiManager wifiManager;

    CheckBox cbBatuk, cbDemam, cbSesakNapas, cbPilek, cbSakitTenggorokan, cbSakitKepala, cbMual, cbLainnya;
    Spinner spGender, spAge;
    String name, email, username, password, phone, gender, age, indication, sLatitude, sLongitude;
    Double latitude, longitude;

    public Register2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register2, container, false);

        cbBatuk = view.findViewById(R.id.cbBatuk);
        cbDemam = view.findViewById(R.id.cbDemam);
        cbSesakNapas = view.findViewById(R.id.cbSesakNapas);
        cbPilek = view.findViewById(R.id.cbPilek);
        cbSakitTenggorokan = view.findViewById(R.id.cbSakitTenggorokan);
        cbSakitKepala = view.findViewById(R.id.cbSakitKepala);
        cbMual = view.findViewById(R.id.cbMual);
        cbLainnya = view.findViewById(R.id.cbLainnya);
        spGender = view.findViewById(R.id.spGender);
        spAge = view.findViewById(R.id.spAge);
        wifiManager= (WifiManager) this.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        view.findViewById(R.id.lyLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PlacePickerActivity.class));
            }
        });

        spinnerSetup();

        view.findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = getArguments().getString(EXTRA_NAME);
                email = getArguments().getString(EXTRA_EMAIL);
                username = getArguments().getString(EXTRA_USERNAME);
                password = getArguments().getString(EXTRA_PASSWORD);
                phone = getArguments().getString(EXTRA_PHONE);
                indication = indicationCheckbox();

                if (indication.equals("")){
                    indication = "Tidak ada";
                }

                latitude = Double.parseDouble(RegisterUtils.getKeyRegisterLatitude(getContext()));
                longitude =  Double.parseDouble(RegisterUtils.getKeyRegisterLongitude(getContext()));

                Log.d("apapun", name+" "+email+" "+username+" "+password+" "+phone+" "+gender+" "+age+" "+indication+"\n"+latitude+" "+longitude);
//                RegisterUtils.setRegisterLocation(getContext(), 0, 0);
            }
        });

        return view;
    }

    private void spinnerSetup() {
        final ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.spin_gender));
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapterGender);

        final ArrayAdapter<String> adapterAge = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.spin_age));
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAge.setAdapter(adapterAge);

        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                age = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String indicationCheckbox() {
        String s = "";

        if (cbBatuk.isChecked()){
            s += "Batuk, ";
        }
        if (cbDemam.isChecked()){
            s += "Demam, ";
        }
        if (cbSesakNapas.isChecked()){
            s += "Sesak Napas, ";
        }
        if (cbPilek.isChecked()){
            s += "Pilek, ";
        }
        if (cbSakitTenggorokan.isChecked()){
            s += "Sakit Tenggorokan, ";
        }
        if (cbSakitKepala.isChecked()){
            s += "Sakit Kepala, ";
        }
        if (cbMual.isChecked()){
            s += "Mual, ";
        }
        if (cbLainnya.isChecked()){
            s += "Lainnya, ";
        }

        return s;
    }
}