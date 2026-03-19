package com.OptiDoc.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // Réduit à 2 secondes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Pour Android 12+, utiliser la nouvelle API mais passer rapidement
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
            // Passer rapidement à l'écran principal pour Android 12+
            super.onCreate(savedInstanceState);
            navigateToMain();
            return;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setupCustomSplash();
    }

    private void setupCustomSplash() {
        ImageView logoImageView = findViewById(R.id.splash_logo);

        // Afficher immédiatement les éléments
        logoImageView.setAlpha(1.0f);


        // Animation de fade in
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(800); // Animation plus rapide

        logoImageView.startAnimation(fadeIn);

        // Navigation plus rapide
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToMain();
            }
        }, SPLASH_DURATION);
    }

    private void navigateToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        // Animation de transition plus fluide
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}