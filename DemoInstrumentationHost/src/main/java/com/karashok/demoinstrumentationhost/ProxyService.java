package com.karashok.demoinstrumentationhost;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.karashok.demoinstrumentationbase.Constants;
import com.karashok.demoinstrumentationbase.IService;

import java.lang.reflect.Constructor;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-03-2022
 */
public class ProxyService extends Service {

    private String className;
    private IService service;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        init(intent);
        return null;
    }

    private void init(Intent intent) {
        className = intent.getStringExtra(Constants.KEY_SERVICE_NAME);
        try{
            Class<?> aClass = PluginManager.getInstance().getDexClassLoader().loadClass(className);
            Constructor<?> constructor = aClass.getConstructor(new Class[]{});
            Object instance = constructor.newInstance(new Object[]{});
            service = (IService) instance;
            service.attach(this);

            service.onCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (service == null) {
            init(intent);
        }
        return service.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        service.onUnbind(intent);
        return super.onUnbind(intent);
    }
}
