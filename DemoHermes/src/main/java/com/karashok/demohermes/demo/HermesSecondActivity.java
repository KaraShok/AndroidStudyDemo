package com.karashok.demohermes.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.karashok.demohermes.Hermes;
import com.karashok.demohermes.HermesService;
import com.karashok.demohermes.R;

public class HermesSecondActivity extends AppCompatActivity {

    private IUserManager instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hermes_second);
        Hermes.getInstance().connect(this, HermesService.class);
        findViewById(R.id.get_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        instance = Hermes.getInstance().getInstance(IUserManager.class);
                        Log.d("DemoHermes", "HermesSecondActivity:onClick: " + instance.hashCode());
//                        Log.d("DemoHermes", "HermesSecondActivity -> " + instance.getFriend().toString());
                    }
                });
        findViewById(R.id.send_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        instance.setFriend(new Friend("DemoSecond",22));
                    }
                });
    }
}