package com.OptiDoc.app;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class MyApplication extends Application {
    private static final String PREFS = "prefs";
    private static final String NIGHT_MODE = "night_mode";

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        boolean nightMode = prefs.getBoolean(NIGHT_MODE, false);
        AppCompatDelegate.setDefaultNightMode(
                nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}