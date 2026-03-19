package com.OptiDoc.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchNight;
    private static final String PREFS     = "prefs";
    private static final String NIGHT_MODE = "night_mode";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.statusBars());

        // Spinner langue
        Spinner spinner = findViewById(R.id.langue);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.langues_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Mode sombre
        sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        switchNight = findViewById(R.id.switchNight);
        boolean nightMode = sharedPreferences.getBoolean(NIGHT_MODE, false);
        switchNight.setChecked(nightMode);
        switchNight.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(NIGHT_MODE, isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        // ✅ Navigation - icône Accueil → MainActivity
        ImageView iconHome = findViewById(R.id.iconHome);
        iconHome.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // ✅ Bouton Quitter → modal de confirmation
        LinearLayout btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("Quitter")
                    .setMessage("Vous voulez vraiment quitter ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        finishAffinity();
                        System.exit(0);
                    })
                    .setNegativeButton("Non", (dialog, which) -> dialog.dismiss())
                    .setCancelable(true)
                    .show();
        });
    }
}