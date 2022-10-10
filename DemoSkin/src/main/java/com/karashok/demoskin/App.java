package com.karashok.demoskin;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.karashok.demoskincore.SkinManager;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-18-2022
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);

        // 日夜间切换，根据app上次退出的状态来判断是否需要设置夜间模式,提前在SharedPreference中存了一个是
//        AppCompatDelegate.setDefaultNightMode();
    }
}
