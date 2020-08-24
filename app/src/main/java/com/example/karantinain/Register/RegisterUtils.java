package com.example.karantinain.Register;

import android.content.Context;
import android.preference.PreferenceManager;

public class RegisterUtils {
    static final String KEY_REGISTER_LATITUDE = "register_latitude";
    static final String KEY_REGISTER_LONGITUDE = "register_longitude";

    static void setRegisterLocation(Context context, double latitude, double longitude) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_REGISTER_LATITUDE, String.valueOf(latitude))
                .putString(KEY_REGISTER_LONGITUDE, String.valueOf(longitude))
                .apply();
    }

    static String getKeyRegisterLatitude(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_REGISTER_LATITUDE, "");
    }

    static String getKeyRegisterLongitude(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_REGISTER_LONGITUDE, "");
    }
}
