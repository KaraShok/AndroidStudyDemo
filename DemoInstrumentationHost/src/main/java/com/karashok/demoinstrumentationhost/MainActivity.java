package com.karashok.demoinstrumentationhost;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.karashok.demoinstrumentationbase.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String ACTION = "com.karashok.demoinstrumentationhost.host_broadcast_action";

    private BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"我是宿主，收到你的消息,握手完成!", Toast.LENGTH_SHORT).show();
        }
    };

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

        findViewById(R.id.load_plugin_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File pluginDir = getDir("plugin", Context.MODE_PRIVATE);
                        String name = "plugin.apk";
                        File pluginFile = new File(pluginDir, name);
                        String pluginPath = pluginFile.getAbsolutePath();
                        if (pluginFile.exists()) {
                            pluginFile.delete();
                        }

                        FileInputStream is = null;
                        FileOutputStream os = null;
                        try{
                            is = new FileInputStream(new File(Environment.getExternalStorageDirectory(),name));
                            os = new FileOutputStream(pluginPath);
                            int len = 0;
                            byte[] buffer = new byte[4096];
                            while ((len = is.read(buffer)) != -1) {
                                os.write(buffer,0,len);
                            }
                            File f = new File(pluginPath);
                            if (f.exists()) {
                                Toast.makeText(MainActivity.this, "dex overwrite", Toast.LENGTH_SHORT).show();
                            }
                            PluginManager.getInstance().loadPlugin(MainActivity.this,pluginPath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally{
                            try{
                                if (is != null) {
                                    is.close();
                                }
                                if (os != null) {
                                    os.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        findViewById(R.id.intent_plugin_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ProxyActivity.class);
                        intent.putExtra(Constants.KEY_CLASS_NAME, PluginManager.getInstance().getPackageInfo().activities[0].name);
                        startActivity(intent);
                    }
                });

        findViewById(R.id.send_plugin_broadcast_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction("com.karashok.demoinstrumentation1.StaticBroadcast");
                        sendBroadcast(intent);
                    }
                });
        registerReceiver(broadcast, new IntentFilter(ACTION));
    }
}