package com.karashok.demouistudy.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.karashok.demouistudy.R;
import com.karashok.demouistudy.widget.path_measure.WaveView;

import android.os.Bundle;

public class DemoPathMeasureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_path_measure);
        WaveView wv = findViewById(R.id.activity_demo_path_measure_wv);
        wv.startAnimation();
    }
}