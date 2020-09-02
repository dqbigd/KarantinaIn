package com.example.karantinain.Main.Insight;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class InsightUtils{
    static final String KEY_INSIGHT_CATEGORY = "insight_category";

    static void setCategory(Context context, String category) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_INSIGHT_CATEGORY, category)
                .apply();
    }

    static String getCategory(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_INSIGHT_CATEGORY, "all");
    }


}
