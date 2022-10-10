package com.karashok.demoinstrumentation1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        findViewById(R.id.intent_activity_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(proxyActivity,SecondActivity.class));
                    }
                });

        findViewById(R.id.intent_service_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startService(new Intent(proxyActivity,MyService.class));
                    }
                });

        findViewById(R.id.register_broadcast_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentFilter intentFilter  = new IntentFilter();
                        intentFilter.addAction(MyReceiver.ACTION);
                        registerReceiver(new MyReceiver(), intentFilter);
                    }
                });

        findViewById(R.id.send_broadcast_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(MyReceiver.ACTION);
                        sendBroadcast(intent);
                    }
                });
    }
}