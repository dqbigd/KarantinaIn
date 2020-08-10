package com.example.karantinain.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefManager {
    static final String KEY_ACCOUNT_USERNAME = "account_username";
    static final String KEY_ACCOUNT_PASSWORD = "account_password";
    static final String KEY_SIGNIN_STATUS = "signin_status";
    static final String KEY_TOKEN = "token";
    static final String KEY_PROFILE_USERNAME = "profile_username";
    static final String KEY_PROFILE_FULLNAME = "profile_fullname";
    static final String KEY_PROFILE_PHONE = "profile_phone";
    static final String KEY_PROFILE_GENDER = "profile_gender";
    static final String KEY_PROFILE_AGE = "profile_age";
    static final String KEY_PROFILE_INDICATION = "profile_indication";

    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    //save data account(for re-login)
    public static void setAccount(Context context, String username,  String password) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_ACCOUNT_USERNAME, username);
        editor.putString(KEY_ACCOUNT_PASSWORD, password);
        editor.apply();
    }

    public static String getKeyAccountUser(Context context) {
        return getSharedPreference(context).getString(KEY_ACCOUNT_USERNAME, "");
    }

    public static String getKeyAccountPassword(Context context) {
        return getSharedPreference(context).getString(KEY_ACCOUNT_PASSWORD, "");
    }

    //save login status(auto login to dashboard)
    public static void setLoggedInStatus(Context context, boolean status, String token) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_SIGNIN_STATUS, status);
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public static boolean getKeySignInStatus(Context context) {
        return getSharedPreference(context).getBoolean(KEY_SIGNIN_STATUS, false);
    }

    public static String getKeyToken(Context context) {
        return getSharedPreference(context).getString(KEY_TOKEN, "");
    }

    public static void setProfile(Context context, String username,String fullname, String phone, String gender, String age, String indication) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_PROFILE_USERNAME, username);
        editor.putString(KEY_PROFILE_FULLNAME, fullname);
        editor.putString(KEY_PROFILE_PHONE, phone);
        editor.putString(KEY_PROFILE_GENDER, gender);
        editor.putString(KEY_PROFILE_AGE, age);
        editor.putString(KEY_PROFILE_INDICATION, indication);
        editor.apply();
    }

    public static String getUserNameProfile(Context context) {
        return getSharedPreference(context).getString(KEY_PROFILE_USERNAME, "");
    }

    public static String getFullNameProfile(Context context) {
        return getSharedPreference(context).getString(KEY_PROFILE_FULLNAME, "");
    }

    public static String getPhoneProfile(Context context) {
        return getSharedPreference(context).getString(KEY_PROFILE_PHONE, "");
    }

    public static String getGenderProfile(Context context) {
        return getSharedPreference(context).getString(KEY_PROFILE_GENDER, "");
    }

    public static String getAgeProfile(Context context) {
        return getSharedPreference(context).getString(KEY_PROFILE_AGE, "");
    }

    public static String getIndicationProfile(Context context) {
        return getSharedPreference(context).getString(KEY_PROFILE_INDICATION, "");
    }
}
