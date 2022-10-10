package com.karashok.demoskincore;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.karashok.demoskincore.utils.SkinPreference;
import com.karashok.demoskincore.utils.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-19-2022
 */
public class SkinManager extends Observable {


    public static void init(Application application) {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(application);
                }
            }
        }
    }

    public static SkinManager getInstance() {
        return instance;
    }

    private static SkinManager instance;

    private SkinActivityLifecycle activityLifecycle;
    private Application application;

    public SkinManager(Application application) {
        this.activityLifecycle = new SkinActivityLifecycle();
        this.application = application;
        SkinPreference.init(application);
        SkinResources.init(application);
        application.registerActivityLifecycleCallbacks(activityLifecycle);
    }

    public void loadSkin(String skinPath) {
        if (TextUtils.isEmpty(skinPath)) {
            // 记录使用默认皮肤
            SkinPreference.getInstance().setSkin("");
            // 清空资源管理器 皮肤资源属性
            SkinResources.getInstance().reset();
        } else {
            try{

                // 反射创建AssetManager 与 Resource
                AssetManager assetManager = AssetManager.class.newInstance();

                // 资源路径设置 目录或压缩包
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",
                        String.class);
                addAssetPath.invoke(assetManager,skinPath);
                Resources appResources = application.getResources();

                // 根据当前的显示与配置(横竖屏、语言等)创建Resources
                Resources skinResources = new Resources(assetManager,appResources.getDisplayMetrics(),appResources.getConfiguration());

                // 记录
                SkinPreference.getInstance().setSkin(skinPath);

                // 获取外部Apk(皮肤包) 包名
                PackageManager packageManager = application.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageArchiveInfo(skinPath,
                        PackageManager.GET_ACTIVITIES);
                SkinResources.getInstance().applySkin(skinResources,packageInfo.packageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setChanged();
        // 通知采集的View 更新皮肤
        // 被观察者改变 通知所有观察者
        notifyObservers(null);
    }

    public void updateSkin(Activity activity) {
        activityLifecycle.updateSkin(activity);
    }
}
