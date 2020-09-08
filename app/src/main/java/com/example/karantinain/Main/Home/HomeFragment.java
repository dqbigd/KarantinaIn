package com.example.karantinain.Main.Home;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karantinain.Api.InitRetrofit;
import com.example.karantinain.BuildConfig;
import com.example.karantinain.Login.LoginActivity;
import com.example.karantinain.Login.LoginResponse;
import com.example.karantinain.Main.MainActivity;
import com.example.karantinain.R;
import com.example.karantinain.Utils.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE_LOCATION = 34;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE_CAMERA = 35;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    TextView tvName, tvLocation, tvSelanjutnya, tvImageDesc;
    Button btnActivateLocation, btnInactiveLocation, btnUploadPhoto;
    RecyclerView rvKegiatan, rvContent;
    ProgressBar pbKegiatan, pbContent;
    RelativeLayout btnEmergency;

    private ArrayList<RecommendActivityData> recommendActivityDataArrayList = new ArrayList<>();
    private RecommendActivityAdapter recommendActivityAdapter;
    private ArrayList<ContentEducationData> contentEducationDataArrayList = new ArrayList<>();
    private ContentEducationAdapter contentEducationAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvName = view.findViewById(R.id.tvName);
        tvImageDesc = view.findViewById(R.id.tvImageDesc);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvSelanjutnya = view.findViewById(R.id.tvFoodHistory);
        btnEmergency = view.findViewById(R.id.btnEmergency);
        btnActivateLocation = (Button) view.findViewById(R.id.btnActivateLocation);
        btnInactiveLocation = view.findViewById(R.id.btnInactiveLocation);
        btnUploadPhoto = view.findViewById(R.id.btnUploadPhoto);
        rvKegiatan = view.findViewById(R.id.rvKegiatan);
        pbKegiatan = view.findViewById(R.id.pbKegiatan);
        rvContent = view.findViewById(R.id.rvContent);
        pbContent = view.findViewById(R.id.pbContent);

        setupRecommendActivity();
        setupContentEducation();

        tvName.setText("Hai, "+ SharedPrefManager.getFullNameProfile(getContext()));

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)){
                    requestPermissionCamera();
                }else{
                    openCamera();
                }
            }
        });

        view.findViewById(R.id.imgBtnNotification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        tvSelanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), KegiatanActivity.class));
            }
        });

        myReceiver = new MyReceiver();
        if (LocationUtils.requestingLocationUpdates(getContext())) {
            if (!checkPermissionLocation()) {
                requestPermissionLocation();
            }
        }

        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataEmergency();
//                Toast.makeText(getContext(), "Emergency", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void sendDataEmergency() {
        String token = SharedPrefManager.getKeyToken(getContext());

        Call<EmergencyResponse> call = InitRetrofit.getInstance().emergency(token);
        call.enqueue(new Callback<EmergencyResponse>() {
            @Override
            public void onResponse(Call<EmergencyResponse> call, Response<EmergencyResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equals("Ok.")){
                        showEmergencyDialog();
                        Log.d(TAG, SharedPrefManager.getIdProfile(getContext())+" : user ID : "+response.body().getData().getUserId());
                    }
                }else {
                    Toast.makeText(getActivity(), "Terdapat gangguan pada server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EmergencyResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Gagal mengirimkan data emergency", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
//        call.enqueue(new Callback<LocationResponse>() {
//            @Override
//            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().getMessage().equals("Ok.")){
//                        Log.d(TAG, "location update to API : "+latitude+", "+longitude);
//                    }
//                }else {
//                    Toast.makeText(getActivity(), "Terdapat gangguan pada server", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LocationResponse> call, Throwable t) {
//                Toast.makeText(getActivity(), "Gagal mengirimkan lokasi terkini anda", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onFailure: "+t.getMessage());
//            }
//        });
    }

    private void showEmergencyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Konfirmasi")
                .setMessage("Petugas gugus tugas COVID-19 setempat akan segera menghubungi anda, jika ODP butuh perawatan intensif petugas akan langsung menuju lokasi anda")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
        });
//        builder.create();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setupContentEducation() {
        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContent.setAdapter(contentEducationAdapter);

        pbContent.setVisibility(View.VISIBLE);
        String token = SharedPrefManager.getKeyToken(getContext());

        Call<ContentEducationResponse> call = InitRetrofit.getInstance().contentEducation(token);
        call.enqueue(new Callback<ContentEducationResponse>() {
            @Override
            public void onResponse(Call<ContentEducationResponse> call, Response<ContentEducationResponse> response) {
                if (response.isSuccessful()) {
                    pbContent.setVisibility(View.GONE);
                    if (response.body() != null && response.body().getMessage().equals("Ok.")) {
                        if (response.body().getData().toString().equals("[]")) {
                            Toast.makeText(getContext(), "Kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            contentEducationDataArrayList = new ArrayList<>(response.body().getData());
                            contentEducationAdapter = new ContentEducationAdapter(contentEducationDataArrayList);
                            rvContent.setAdapter(contentEducationAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ContentEducationResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Mohon cek jaringan internet anda", Toast.LENGTH_SHORT).show();
                Log.d("Response Error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void setupRecommendActivity() {
        rvKegiatan.setLayoutManager(new LinearLayoutManager(getContext()));
        rvKegiatan.setAdapter(recommendActivityAdapter);

        pbKegiatan.setVisibility(View.VISIBLE);
        String token = SharedPrefManager.getKeyToken(getContext());

        Call<RecommendActivityResponse> call = InitRetrofit.getInstance().recommendActivity(token);
        call.enqueue(new Callback<RecommendActivityResponse>() {
            @Override
            public void onResponse(Call<RecommendActivityResponse> call, Response<RecommendActivityResponse> response) {
                if (response.isSuccessful()) {
                    pbKegiatan.setVisibility(View.GONE);
                    if (response.body() != null && response.body().getMessage().equals("Ok.")) {
                        if (response.body().getData().toString().equals("[]")) {
                            Toast.makeText(getContext(), "Kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            rvKegiatan.setVisibility(View.VISIBLE);
                            recommendActivityDataArrayList = new ArrayList<>(response.body().getData());
                            recommendActivityAdapter = new RecommendActivityAdapter(recommendActivityDataArrayList,true, 3);
                            rvKegiatan.setAdapter(recommendActivityAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RecommendActivityResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Mohon cek jaringan internet anda", Toast.LENGTH_SHORT).show();
                Log.d("Response Error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void reLogin() {
        pbKegiatan.setVisibility(View.VISIBLE);

        String sUsername = SharedPrefManager.getKeyAccountUser(getContext());
        String sPassword = SharedPrefManager.getKeyAccountPassword(getContext());

        Call<LoginResponse> call = InitRetrofit.getInstance().signIn(sUsername, sPassword);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equals("Ok.")){
                        pbKegiatan.setVisibility(View.GONE);

                        String token = response.body().getData();

                        SharedPrefManager.setLoggedInStatus(getContext(),true, token);
                    }
                }else {
                    Toast.makeText(getContext(), "Tidak dapat melakukan re-login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Mohon cek koneksi internet anda ", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_PERMISSIONS_REQUEST_CODE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE_CAMERA && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ImageUpload(bitmap);
        }
    }

    private void ImageUpload(Bitmap bitmap) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Mengirimkan data");
        dialog.setMessage("Loading ...");
        dialog.setCancelable(true);
        dialog.show();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        String image = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
        String token = SharedPrefManager.getKeyToken(getContext());

        Call<SelfieResponse> call = InitRetrofit.getInstance().selfie(token, image);
        call.enqueue(new Callback<SelfieResponse>() {
            @Override
            public void onResponse(Call<SelfieResponse> call, Response<SelfieResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getMessage().equals("Ok.")){
                        dialog.dismiss();
                        tvImageDesc.setText("Foto telah diupdate");
                        btnUploadPhoto.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_grey_home_rounded));
                        btnUploadPhoto.setEnabled(false);
                        Toast.makeText(getContext(), "Berhasil Upload foto terbaru", Toast.LENGTH_SHORT).show();

                        Log.d("isSuccessSHIT", response.body().getData().getImage());
                    }
                }else{
                    Log.d("isSuccessSHIT", response.message());
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SelfieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                Log.d("onFailureSHIT", t.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        View view = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);

        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE_LOCATION) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService.requestLocationUpdates();
            } else {
                // Permission denied.
                setButtonsState(false);
                Snackbar.make(
                        view,
                        R.string.penjelasan_permission_dibutuhkan,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.pengaturan, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!LocationUtils.lastLocationUpdates(getContext()).equals("")){
            tvLocation.setText(LocationUtils.lastLocationUpdates(getContext()));
        }

        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
        btnActivateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermissionLocation()) {
                    requestPermissionLocation();
                } else {
                    mService.requestLocationUpdates();
                }
            }
        });

        btnInactiveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogInactive();
            }
        });

        // Restore the state of the buttons when the activity (re)launches.
        setButtonsState(LocationUtils.requestingLocationUpdates(getContext()));
        getActivity().bindService(new Intent(getActivity(), LocationUpdatesService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void openDialogInactive() {
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
                LocationUtils.setLastLocationUpdates(getContext(), "");
                tvLocation.setText(R.string.tidak_ada_lokasi_terkini);
                mService.removeLocationUpdates();
            }
        });
//        builder.create();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    public void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            getActivity().unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    private void requestPermissionLocation() {
        View view = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    view,
                    R.string.izin_diperlukan_untuk_kebutuhan_inti,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE_LOCATION);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE_LOCATION);
        }
    }

    private void requestPermissionCamera() {
        View view = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CAMERA);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    view,
                    R.string.izin_diperlukan_untuk_kebutuhan_inti,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_PERMISSIONS_REQUEST_CODE_CAMERA);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISSIONS_REQUEST_CODE_CAMERA);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(LocationUtils.KEY_REQUESTING_LOCATION_UPDATES)) {
            setButtonsState(sharedPreferences.getBoolean(LocationUtils.KEY_REQUESTING_LOCATION_UPDATES, false));
        }
    }

    private boolean checkPermissionLocation() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                Geocoder geocoder = new Geocoder(getContext());
                sendDataLocation(location);
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    tvLocation.setText(addresses.get(0).getAddressLine(0));
                    LocationUtils.setLastLocationUpdates(getContext(), addresses.get(0).getAddressLine(0));
                    Toast.makeText(context, "Berhasil diaktifkan", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(context, "Alamat tidak diketahui", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(MainActivity.this, Utils.getLocationText(location), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendDataLocation(Location location) {
        String token = SharedPrefManager.getKeyToken(getContext());
        String latitude, longitude;
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        Call<LocationResponse> call = InitRetrofit.getInstance().location(token, latitude, longitude);
        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equals("Ok.")){
                        Log.d(TAG, "location update to API : "+latitude+", "+longitude);
                    }
                }else {
                    Toast.makeText(getActivity(), "Terdapat gangguan pada server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Gagal mengirimkan lokasi terkini anda", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void setButtonsState(boolean requestingLocationUpdates) {
        if (requestingLocationUpdates) {
            btnActivateLocation.setEnabled(false);
            btnActivateLocation.setVisibility(View.INVISIBLE);
            btnInactiveLocation.setEnabled(true);
            btnInactiveLocation.setVisibility(View.VISIBLE);
        } else {
            btnActivateLocation.setEnabled(true);
            btnActivateLocation.setVisibility(View.VISIBLE);
            btnInactiveLocation.setEnabled(false);
            btnInactiveLocation.setVisibility(View.GONE);
        }
    }
}