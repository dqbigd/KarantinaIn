package com.example.karantinain.Main.Home;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.karantinain.R;
import com.google.gson.internal.$Gson$Preconditions;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

class LocationUtils {
    Context context;

    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates";
    static final String KEY_LAST_LOCATION_UPDATES = "last_location_updates";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }
    static String lastLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LAST_LOCATION_UPDATES, "");
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    static void setLastLocationUpdates(Context context, String location) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_LAST_LOCATION_UPDATES, location)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    static String getLocationText(Context context, Location location) {
        String sLocation ="";
        if (location == null ){
            sLocation = "Lokasi tidak diketahui";
        }else {
            Geocoder geocoder = new Geocoder(context);
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                sLocation = addresses.get(0).getAddressLine(0);
            }catch (Exception e){
                Toast.makeText(context, "Alamat tidak diketahui", Toast.LENGTH_SHORT).show();
            }
        }

        return sLocation;
    }

    static String getLocationTitle(Context context) {
        return context.getString(R.string.lokasi_terbaru, DateFormat.getDateTimeInstance().format(new Date()));
    }
}
