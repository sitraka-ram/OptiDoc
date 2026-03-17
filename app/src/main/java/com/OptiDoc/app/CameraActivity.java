package com.OptiDoc.app;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {

    private PreviewView previewView;
    private ImageCapture imageCapture;
    private Camera camera;
    private ImageView btnGalerieCamera;
    private boolean isFlashOn = false;
    private static final int PERMISSION_CAMERA = 200;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView      = findViewById(R.id.previewView);
        btnGalerieCamera = findViewById(R.id.btnGalerieCamera);
        ImageButton btnCapture = findViewById(R.id.btnCapture);
        TextView btnFlash      = findViewById(R.id.btnFlash);
        TextView btnAide       = findViewById(R.id.btnAide);

        // ✅ Launcher galerie
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK
                            && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        Toast.makeText(this, "Image sélectionnée ✅", Toast.LENGTH_SHORT).show();
                        // ➡️ Utilise imageUri ici
                    }
                }
        );

        // ✅ Flash toggle
        btnFlash.setOnClickListener(v -> {
            if (camera == null) return;
            isFlashOn = !isFlashOn;
            camera.getCameraControl().enableTorch(isFlashOn);
            btnFlash.setText(isFlashOn ? "💡" : "⚡");
        });

        // ✅ Galerie
        btnGalerieCamera.setOnClickListener(v -> ouvrirGalerie());

        // ✅ Aide
        btnAide.setOnClickListener(v -> {
            Intent intent = new Intent(CameraActivity.this, AideActivity.class);
            startActivity(intent);
        });

        // ✅ Capture photo
        btnCapture.setOnClickListener(v -> prendrePhoto());

        // ✅ Permission caméra
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            demarrerCamera();
            chargerDerniereImage(); // ← charge la dernière image galerie
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
        }
    }

    // ✅ Charge la dernière image de la galerie dans le bouton
    private void chargerDerniereImage() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;

        // Vérifie permission galerie avant de lire
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Pas de permission → on garde l'icône par défaut
            return;
        }

        String[] projection = {MediaStore.Images.Media._ID};
        String sortOrder    = MediaStore.Images.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, sortOrder)) {

            if (cursor != null && cursor.moveToFirst()) {
                long id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));

                Uri derniereImage = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                // Charger dans le bouton avec Glide (cercle)
                Glide.with(this)
                        .load(derniereImage)
                        .circleCrop()
                        .placeholder(R.drawable.galerie)
                        .into(btnGalerieCamera);
            }
        } catch (Exception e) {
            // En cas d'erreur → garde l'icône par défaut
            btnGalerieCamera.setImageResource(R.drawable.galerie);
        }
    }

    private void ouvrirGalerie() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 101);
        } else {
            lancerGalerie();
        }
    }

    private void lancerGalerie() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private void demarrerCamera() {
        ListenableFuture<ProcessCameraProvider> future =
                ProcessCameraProvider.getInstance(this);

        future.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = future.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build();

                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(
                        this,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Erreur caméra ❌", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void prendrePhoto() {
        if (imageCapture == null) {
            Toast.makeText(this, "Caméra non prête", Toast.LENGTH_SHORT).show();
            return;
        }

        File photoFile = new File(
                getExternalFilesDir(null),
                "scan_" + System.currentTimeMillis() + ".jpg"
        );

        ImageCapture.OutputFileOptions options =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(options, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults output) {
                        Toast.makeText(CameraActivity.this,
                                "✅ Photo sauvegardée !", Toast.LENGTH_SHORT).show();
                        // ✅ Rafraîchir le bouton galerie avec la nouvelle photo
                        chargerDerniereImage();
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(CameraActivity.this,
                                "❌ Erreur capture", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (requestCode == PERMISSION_CAMERA) {
            if (granted) { demarrerCamera(); chargerDerniereImage(); }
            else { Toast.makeText(this, "Permission caméra refusée ❌", Toast.LENGTH_SHORT).show(); finish(); }
        } else if (requestCode == 101) {
            if (granted) { lancerGalerie(); chargerDerniereImage(); }
            else Toast.makeText(this, "Permission galerie refusée ❌", Toast.LENGTH_SHORT).show();
        }
    }
}