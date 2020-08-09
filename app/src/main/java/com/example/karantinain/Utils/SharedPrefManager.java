package com.example.karantinain.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefManager {
    static final String KEY_ACCOUNT_USERNAME = "account_username";
    static final String KEY_ACCOUNT_PASSWORD = "account_password";
    static final String KEY_SIGNIN_STATUS = "signin_status";
    static final String KEY_TOKEN = "token";
//    static final String KEY_SIGNIN_AS = "signin_as";
//    static final String KEY_PROFILE_NICKNAME = "profile_nickname";
//    static final String KEY_PROFILE_FULLNAME = "profile_fullname";
//    static final String KEY_PROFILE_NIM = "profile_nim";
//    static final String KEY_PROFILE_JURUSAN = "profile_jurusan";
//    static final String KEY_PROFILE_IMAGE = "profile_img";

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

//    public static void setProfile(Context context, String fullname, String nickname, String nim, String jurusan, String image) {
//        SharedPreferences.Editor editor = getSharedPreference(context).edit();
//        editor.putString(KEY_PROFILE_FULLNAME, fullname);
//        editor.putString(KEY_PROFILE_NICKNAME, nickname);
//        editor.putString(KEY_PROFILE_NIM, nim);
//        editor.putString(KEY_PROFILE_JURUSAN, jurusan);
//        editor.putString(KEY_PROFILE_IMAGE, image);
//        editor.apply();
//    }
//
//    public static String getNickNameProfile(Context context) {
//        return getSharedPreference(context).getString(KEY_PROFILE_NICKNAME, "");
//    }
//
//    public static String getFullNameProfile(Context context) {
//        return getSharedPreference(context).getString(KEY_PROFILE_FULLNAME, "");
//    }
//
//    public static String getNimProfile(Context context) {
//        return getSharedPreference(context).getString(KEY_PROFILE_NIM, "");
//    }
//
//    public static String getJurusanProfile(Context context) {
//        return getSharedPreference(context).getString(KEY_PROFILE_JURUSAN, "");
//    }
//
//    public static String getImageProfile(Context context) {
//        return getSharedPreference(context).getString(KEY_PROFILE_IMAGE, "");
//    }
}
