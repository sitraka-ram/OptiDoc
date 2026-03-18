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

        // Bouton Caméra (ImageView, pas FloatingActionButton)
        ImageView btnCamera = findViewById(R.id.btnCamera);
        if (btnCamera != null) {
            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ResultatActivity.this, "Caméra cliqué", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}