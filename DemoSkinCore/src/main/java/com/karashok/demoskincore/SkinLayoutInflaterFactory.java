package com.karashok.demoskincore;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.karashok.demoskincore.utils.SkinThemeUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-19-2022
 */
public class SkinLayoutInflaterFactory implements LayoutInflater.Factory2, Observer {

    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    private static final Map<String, Constructor<? extends View>> constructorMap = new HashMap<>();

    private static final Class<?>[] constructorSignature = new Class[]{Context.class,AttributeSet.class};

    private SkinAttribute skinAttribute;
    private Activity activity;

    public SkinLayoutInflaterFactory(Activity activity, Typeface typeface) {
        this.skinAttribute = new SkinAttribute(typeface);
        this.activity = activity;
    }

    /**
     * 创建对应布局并返回
     *
     * @param parent  当前TAG 父布局
     * @param name    在布局中的TAG 如:TextView, android.support.v7.widget.Toolbar
     * @param context 上下文
     * @param attrs   对应布局TAG中的属性 如: android:text android:src
     * @return View    null则由系统创建
     */
    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = createViewFromTag(name, context, attrs);
        if (null == view) {
            view = createView(name, context, attrs);
        }
        if (null != view) {
            Log.d("DemoSkinCore", "onCreateView: " + name);
            skinAttribute.load(view,attrs);
        }
        return view;
    }

    private View createViewFromTag(String name, Context context, AttributeSet attrs) {
        // 如果包含 . 则不是SDK中的view 可能是自定义view包括support库中的View
        if (-1 != name.indexOf('.')) {
            return null;
        }
        for (int i = 0; i < mClassPrefixList.length; i++) {
            View view = createView(mClassPrefixList[i] + name, context, attrs);
            return view;
        }
        return null;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = findConstructor(context, name);
        try{
            return constructor.newInstance(context,attrs);
        } catch (Exception e) {

        }
        return null;
    }

    private Constructor<? extends View> findConstructor(Context context, String name) {
        Constructor<? extends View> constructor = constructorMap.get(name);
        if (null == constructor) {
            try{
                Class<? extends View> aClass =
                        context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = aClass.getConstructor(constructorSignature);
                constructorMap.put(name,constructor);
            } catch (Exception e) {

            }
        }
        return constructor;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context,
                             @NonNull AttributeSet attrs) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        SkinThemeUtils.updateStatusBarColor(activity);
        skinAttribute.setTypeface(SkinThemeUtils.getSkinTypeface(activity));
        skinAttribute.applySkin();
    }
}
