package com.example.karantinain.Register;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.karantinain.Api.InitRetrofit;
import com.example.karantinain.BuildConfig;
import com.example.karantinain.Login.LoginActivity;
import com.example.karantinain.Login.LoginResponse;
import com.example.karantinain.Main.MainActivity;
import com.example.karantinain.R;
import com.example.karantinain.Utils.SharedPrefManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register2Fragment extends Fragment {
    public static String EXTRA_NAME = "extra_name";
    public static String EXTRA_EMAIL = "extra_email";
    public static String EXTRA_USERNAME = "extra_username";
    public static String EXTRA_PASSWORD = "extra_password";
    public static String EXTRA_PHONE = "extra_phone";

    WifiManager wifiManager;

    TextView tvLocation;
    CheckBox cbBatuk, cbDemam, cbSesakNapas, cbPilek, cbSakitTenggorokan, cbSakitKepala, cbMual, cbLainnya;
    Spinner spGender, spAge;
    String name, email, username, password, phone, gender, age, indication, latitude, longitude;
    LatLng latLng;

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
        tvLocation = view.findViewById(R.id.tvLocation);
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

                latitude = RegisterUtils.getKeyRegisterLatitude(getContext());
                longitude = RegisterUtils.getKeyRegisterLongitude(getContext());
//                latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
//                tvLocation.setText(getAddress(latLng));

                sendData(name, email, username, password, phone, gender, age, indication, latitude, longitude);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!RegisterUtils.getKeyRegisterLatitude(getContext()).equals("0.0")){
            latLng = new LatLng(Double.parseDouble(RegisterUtils.getKeyRegisterLatitude(getContext())), Double.parseDouble(RegisterUtils.getKeyRegisterLongitude(getContext())));
            tvLocation.setTextColor(Color.parseColor("#000000"));
            tvLocation.setText(getAddress(latLng));
        }
    }

    private void sendData(String name, String email, String username, String password, String phone, String gender, String age, String indication, String latitude, String longitude) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Mengirimkan data");
        dialog.setMessage("Loading ...");
        dialog.setCancelable(true);
        dialog.show();

        Call<RegisterResponse> call = InitRetrofit.getInstance().signUp(name, email, username, password, phone, gender, age, indication, latitude, longitude);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equals("Ok.")){
                        dialog.dismiss();

                        RegisterUtils.setRegisterLocation(getContext(), 0, 0);
                        Toast.makeText(getContext(), "Pendaftaran berhasil", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }else{
                        Toast.makeText(getContext(), "Terjadi masalah, harap lakukan pendaftaran ulang", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Mohon cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                Log.d("RegisterFragment", "onFailure: "+t.getMessage());
            }
        });
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

    private String getAddress(LatLng latLng){

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return "Alamat tidak diketahui";

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RegisterUtils.setRegisterLocation(getContext(), 0, 0);
    }
}