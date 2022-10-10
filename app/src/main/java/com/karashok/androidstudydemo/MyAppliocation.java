package com.karashok.androidstudydemo;

import android.app.Application;

import com.karashok.demoroutercore.DRouter;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-16-2022
 */
public class MyAppliocation extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            DRouter.init(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
