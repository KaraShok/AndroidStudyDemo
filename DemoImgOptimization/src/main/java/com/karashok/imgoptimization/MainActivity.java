package com.karashok.imgoptimization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        
        ImageCache.getInstance().init(this, Environment.getExternalStorageDirectory()+"/ImgOptimization");
        RecyclerView rv = findViewById(R.id.content_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ContentAdapter(getItemData()));
    }

    private List<String> getItemData() {
        List<String> itemData = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            itemData.add(String.valueOf(i));
        }
        return itemData;
    }
}