package com.karashok.demohermes.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.karashok.demohermes.R;

import com.karashok.demohermes.Hermes;

public class HermesMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hermes_main);

        Hermes.getInstance().register(DemoUserManager.class);
        DemoUserManager.getInstance().setFriend(new Friend("DemoMain",18));
    Log.d("DemoHermes", "HermesMainActivity:onCreate: " + DemoUserManager.getInstance().hashCode());
        findViewById(R.id.intent_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(HermesMainActivity.this,HermesSecondActivity.class));
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("DemoHermes", "HermesMainActivity -> " + DemoUserManager.getInstance().getFriend().toString());
    }
}