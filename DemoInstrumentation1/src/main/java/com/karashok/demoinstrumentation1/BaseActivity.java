package com.karashok.demoinstrumentation1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.karashok.demoinstrumentationbase.Constants;
import com.karashok.demoinstrumentationbase.IActivity;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-03-2022
 */
public class BaseActivity extends AppCompatActivity implements IActivity {

    protected Activity proxyActivity;

    @Override
    public void attach(Activity proxyActivity) {
        this.proxyActivity = proxyActivity;
    }

    @Override
    public void setContentView(View view) {
        if (proxyActivity != null) {
            proxyActivity.setContentView(view);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (proxyActivity != null) {
            proxyActivity.setContentView(layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (proxyActivity != null) {
            return proxyActivity.findViewById(id);
        } else {
            return super.findViewById(id);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (proxyActivity != null) {
            Intent res = new Intent();
            res.putExtra(Constants.KEY_CLASS_NAME,intent.getComponent().getClassName());
            proxyActivity.startActivity(res);
        } else {
            super.startActivity(intent);
        }
    }

    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        if (proxyActivity != null) {
            Intent res = new Intent();
            res.putExtra(Constants.KEY_SERVICE_NAME,service.getComponent().getClassName());
            return proxyActivity.startService(res);
        } else {
            return super.startService(service);
        }
    }

    @Override
    public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter) {
        if (proxyActivity != null) {
            return proxyActivity.registerReceiver(receiver, filter);
        } else {
            return super.registerReceiver(receiver, filter);
        }
    }

    @Override
    public void sendBroadcast(Intent intent) {
        if (proxyActivity != null) {
            proxyActivity.sendBroadcast(intent);
        } else {
            super.sendBroadcast(intent);
        }
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {

    }
}
