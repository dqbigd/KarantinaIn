package com.example.karantinain.Main.Home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karantinain.Main.MainActivity;
import com.example.karantinain.R;
import com.example.karantinain.Utils.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

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

    TextView tvName, tvLocation, tvSelanjutnya;
    Button btnActivateLocation, btnInactiveLocation, btnUploadPhoto;

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
        tvLocation = view.findViewById(R.id.tvLocation);
        tvSelanjutnya = view.findViewById(R.id.tvFoodHistory);
        btnActivateLocation = (Button) view.findViewById(R.id.btnActivateLocation);
        btnInactiveLocation = view.findViewById(R.id.btnInactiveLocation);
        btnUploadPhoto = view.findViewById(R.id.btnUploadPhoto);

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

        return view;
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_PERMISSIONS_REQUEST_CODE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

//        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE_CAMERA){
//
//        }
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