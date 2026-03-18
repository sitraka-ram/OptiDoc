package com.OptiDoc.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Launcher Galerie
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK
                            && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        Toast.makeText(this, "Image sélectionnée ✅", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Bouton Galerie
        LinearLayout btnGalerie = findViewById(R.id.btnGalerie);
        btnGalerie.setOnClickListener(v -> openGallery());

        // Bouton Scanner → CameraActivity
        LinearLayout btnScanner = findViewById(R.id.btnScanner);
        btnScanner.setOnClickListener(v -> ouvrirCamera());

        // Bouton Diaphragme → CameraActivity
        ImageView btnCamera = findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(v -> ouvrirCamera());

        ImageView btnsetting = findViewById(R.id.btnsetting);
        btnsetting.setOnClickListener(v -> ouvrirSetting());

        // Bouton Giraffe PDF → ResultatActivity
        LinearLayout btnGiraffePdf = findViewById(R.id.btnGiraffePdf);
        btnGiraffePdf.setOnClickListener(v -> ouvrirResultat());
    }

    private void ouvrirCamera() {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    private void ouvrirResultat() {
        Intent intent = new Intent(MainActivity.this, ResultatActivity.class);
        intent.putExtra("animal_name", "Girafe");
        intent.putExtra("source", "pdf_recent");
        startActivity(intent);
    }
    private void ouvrirSetting() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            } else {
                lancerGalerie();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            } else {
                lancerGalerie();
            }
        }
    }

    private void lancerGalerie() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            lancerGalerie();
        } else {
            Toast.makeText(this, "Permission refusée ❌", Toast.LENGTH_SHORT).show();
        }
    }
}