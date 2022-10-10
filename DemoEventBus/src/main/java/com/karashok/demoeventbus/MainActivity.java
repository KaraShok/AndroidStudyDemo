package com.karashok.demoeventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DemoEventBus.getDefault().register(this);
        findViewById(R.id.demo_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DemoEventBus.getDefault().post("sdfsdf");
                            }
                        }).start();

                    }
                });
    }

    @DemoSubscribe(threadMode = DemoThreadMode.MAIN)
    public void demoEvent(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DemoEventBus.getDefault().unregister(this);
    }
}