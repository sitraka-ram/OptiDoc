package com.OptiDoc.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ResultatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);


        // ✅ Navigation - icône Accueil → MainActivity
        ImageView iconHome = findViewById(R.id.iconHome);
        iconHome.setOnClickListener(v -> {
            Intent intent = new Intent(ResultatActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        //Ouvrir le setting

        ImageView btnsetting = findViewById(R.id.btnsetting);
        btnsetting.setOnClickListener(v -> ouvrirSetting());


        // Bouton Créer Poster
        AppCompatButton btnCreerPoster = findViewById(R.id.btnCreerPoster);
        if (btnCreerPoster != null) {
            btnCreerPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ResultatActivity.this, "Créer Poster cliqué", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Bouton Export PDF
        AppCompatButton btnExportPdf = findViewById(R.id.btnExportPdf);
        if (btnExportPdf != null) {
            btnExportPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ResultatActivity.this, "Export PDF cliqué", Toast.LENGTH_SHORT).show();
                }
            });
        }



    }

    private void ouvrirSetting() {
        Intent intent = new Intent(ResultatActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}