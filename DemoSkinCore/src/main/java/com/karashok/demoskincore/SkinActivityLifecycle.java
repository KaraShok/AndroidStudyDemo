package com.karashok.demoskincore;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.karashok.demoskincore.utils.SkinThemeUtils;

import java.lang.reflect.Field;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-19-2022
 */
public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private ArrayMap<Activity,SkinLayoutInflaterFactory> layoutFactories = new ArrayMap<>();

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        /**
         *  更新状态栏
         */
        SkinThemeUtils.updateStatusBarColor(activity);

        /**
         * 更新字体
         */
        Typeface typeface = SkinThemeUtils.getSkinTypeface(activity);

        /**
         *  更新布局视图
         */
        // 获得 Activity 的布局加载器
        LayoutInflater inflater = LayoutInflater.from(activity);
        try{
            // Android 布局加载器 使用 mFactorySet 标记是否设置过Factory
            // 如设置过抛出一次
            // 设置 mFactorySet 标签为false
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(inflater,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用 factory2 设置布局加载工程
        SkinLayoutInflaterFactory factory = new SkinLayoutInflaterFactory(activity,typeface);
        inflater.setFactory2(factory);
        layoutFactories.put(activity,factory);
        SkinManager.getInstance().addObserver(factory);
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        SkinLayoutInflaterFactory factory = layoutFactories.remove(activity);
        SkinManager.getInstance().deleteObserver(factory);
    }

    public void updateSkin(Activity activity) {
        SkinLayoutInflaterFactory factory = layoutFactories.get(activity);
        factory.update(null,null);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }
}
