package com.karashok.demoskin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int REQUEST_CODE_PERMISSION_STORAGE = 100;
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        for (String str : permissions) {
            if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(permissions, REQUEST_CODE_PERMISSION_STORAGE);
                return;
            }
        }
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}