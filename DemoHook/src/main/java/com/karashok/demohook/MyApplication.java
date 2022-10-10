package com.karashok.demohook;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-04-2022
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            DemoAMSCheckEngine.mHookAMS(this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            DemoActivityThread demoActivityThread = new DemoActivityThread(this);
            demoActivityThread.mActivityThreadmHAction(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
