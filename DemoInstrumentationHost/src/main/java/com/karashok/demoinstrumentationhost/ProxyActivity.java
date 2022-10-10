package com.karashok.demoinstrumentationhost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;

import com.karashok.demoinstrumentationbase.Constants;
import com.karashok.demoinstrumentationbase.IActivity;

import java.lang.reflect.Constructor;

public class ProxyActivity extends AppCompatActivity {

    private String className;
    private IActivity iActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        className = getIntent().getStringExtra(Constants.KEY_CLASS_NAME);

        try{
            Class<?> aClass = PluginManager.getInstance().getDexClassLoader().loadClass(className);
            Constructor<?> constructor = aClass.getConstructor(new Class[]{});
            Object instance = constructor.newInstance(new Object[]{});
            iActivity = (IActivity) instance;
            iActivity.attach(this);
            iActivity.onCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        String cName = intent.getStringExtra(Constants.KEY_CLASS_NAME);
        Intent res = new Intent(this,ProxyActivity.class);
        res.putExtra(Constants.KEY_CLASS_NAME,cName);
        super.startActivity(res);
    }

    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        String serviceName = service.getStringExtra(Constants.KEY_SERVICE_NAME);
        Intent intent = new Intent(this, ProxyService.class);
        intent.putExtra(Constants.KEY_SERVICE_NAME, serviceName);
        return super.startService(intent);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        IntentFilter intentFilter = new IntentFilter();
        for (int i = 0; i < filter.countActions(); i++) {
            intentFilter.addAction(filter.getAction(i));
        }
        return super.registerReceiver(new ProxyBroadcast(receiver.getClass().getName(),
                this), intentFilter);
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader();
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getResources();
    }
}