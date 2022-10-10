package com.karashok.demoinstrumentationhost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.karashok.demoinstrumentationbase.IBroadcast;

import java.lang.reflect.Constructor;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-03-2022
 */
public class ProxyBroadcast extends BroadcastReceiver {

    private String className;
    private IBroadcast broadcast;

    /**
     * app 启动
     * 手机启动
     */
    public ProxyBroadcast() {
    }

    public ProxyBroadcast(String className, Context context) {
        this.className = className;
        try{
            Class<?> aClass = PluginManager.getInstance().getDexClassLoader().loadClass(className);
            Constructor<?> constructor = aClass.getConstructor(new Class[]{});
            Object instance = constructor.newInstance(new Object[]{});
            broadcast = (IBroadcast) instance;
            broadcast.attach(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        broadcast.onReceive(context,intent);
    }
}
