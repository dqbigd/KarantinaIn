package com.example.karantinain.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karantinain.BuildConfig;
import com.example.karantinain.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PlacePickerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = PlacePickerActivity.class.getSimpleName();

    private static final int DEFAULT_ZOOM = 15;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE_LOCATION = 34;
    private boolean permissionDinied = false;
    private LocationManager locationManager;

    private final LatLng mDefaultLocation = new LatLng(-0.7893, 113.9213);
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLocation;
    private GoogleMap mMap;

    RelativeLayout lySelectLocation;
    TextView tvLocationName;
    Button btnSelectLocation;
    ImageButton imgBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);

        lySelectLocation = findViewById(R.id.lySelectLocation);
        tvLocationName = findViewById(R.id.tvLocationName);
        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        imgBtnBack = findViewById(R.id.imgBtnBack);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

//        String apiKey = getString(R.string.google_maps_api);
//        Places.initialize(getApplicationContext(), apiKey);
//        mPlacesClient = Places.createClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        //still checking the permissions in case the user turn them of while the activity is in pause
        //here you can weather "onMissingPermissionError" or "checkPermissions"; both can do the job
        //    checkPermission();
        if (permissionDinied){
            permissionDinied = false;
        }else{
            if (mMap != null){
                //check if the gps is enabled
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    mMap.setMyLocationEnabled(true);
                }else{
                    //show an alert to ask him enable his GPS captor
                    alertMessageEnableGPS();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkPermission();
        getDeviceLocation();

        LatLng ltlng=new LatLng(-0.7893, 113.9213);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ltlng, DEFAULT_ZOOM);
        mMap.animateCamera(cameraUpdate);

        // Enable the zoom controls for the map
        mMap.getUiSettings().setZoomControlsEnabled(true);

        selectNewAdress();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE_LOCATION) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                checkPermission();
            } else {
                // Permission denied.
                requestPermissions();
                permissionDinied = true;
//                Snackbar.make(
//                        findViewById(R.id.activity_main),
//                        R.string.izin_diperlukan_untuk_kebutuhan_inti,
//                        Snackbar.LENGTH_INDEFINITE)
//                        .setAction(R.string.pengaturan, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                // Build intent that displays the App settings screen.
//                                Intent intent = new Intent();
//                                intent.setAction(
//                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                Uri uri = Uri.fromParts("package",
//                                        BuildConfig.APPLICATION_ID, null);
//                                intent.setData(uri);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
//                        })
//                        .show();
            }
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (!permissionDinied) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLocation = task.getResult();
                            if (mLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLocation.getLatitude(),
                                                mLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            LatLng ltlng = new LatLng(-0.7893, 113.9213);
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(ltlng, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void selectNewAdress() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                lySelectLocation.setVisibility(View.VISIBLE);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                tvLocationName.setText(getAddress(latLng));

                btnSelectLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double latitude, longitude;

                        if (latLng !=null){
                            latitude = latLng.latitude;
                            longitude = latLng.longitude;

                            Log.d("hiya2", latitude+" "+longitude);

                            nextFragment(latitude, longitude);
                        }else {
                            Toast.makeText(PlacePickerActivity.this, "Anda belum memilih lokasi", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                mMap.clear();
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 19.0f);
                mMap.animateCamera(location);
                mMap.addMarker(markerOptions);
            }
        });
    }

    private void nextFragment(double latitude, double longitude) {
//        Register2Fragment register2Fragment = new Register2Fragment();

        RegisterUtils.setRegisterLocation(getApplicationContext(), latitude, longitude);

//        Bundle bundle = new Bundle();
//        bundle.putDouble(Register2Fragment.EXTRA_LATITUDE, latitude);
//        bundle.putDouble(Register2Fragment.EXTRA_LONGITUDE, longitude);
//
//        register2Fragment.setArguments(bundle);

        finish();
    }

    private void checkPermission(){
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)&&(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            //if i have both of the required permissions
            if (mMap != null){
                //check if the gps is enabled
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    mMap.setMyLocationEnabled(true);
                }else{
                    //show an alert to ask him enable hiq GPS captor
                    alertMessageEnableGPS();
                }
            }
        }else {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.izin_diperlukan_untuk_kebutuhan_inti,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(PlacePickerActivity.this,
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
            ActivityCompat.requestPermissions(PlacePickerActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE_LOCATION);
        }
    }

    private void alertMessageEnableGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS anda sepertinya belum dinyalakan, segera hidupkan untuk menggunakan semua fitur")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private String getAddress(LatLng latLng){

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

//            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
//            if (prev != null) {
//                fragmentTransaction.remove(prev);
//            }
//            fragmentTransaction.addToBackStack(null);
//            DialogFragment dialogFragment = new ConfirmAddress();

//            Bundle args = new Bundle();
//            args.putDouble("lat", latLng.latitude);
//            args.putDouble("long", latLng.longitude);
//            args.putString("address", address);
//            dialogFragment.setArguments(args);
//            dialogFragment.show(fragmentTransaction, "dialog");
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return "Alamat tidak diketahui";

        }
    }

}