package com.karashok.demoglide.glide.manager;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author KaraShokZ.
 * @des 生命周期监听管理器
 * @since 07-17-2022
 */
public class ActivityFragmentLifecycle implements Lifecycle {

    private final Set<LifecycleListener> lifecycleListeners =
            Collections.newSetFromMap(new WeakHashMap<LifecycleListener, Boolean>());

    // 已启动
    private boolean isStarted;

    // 已销毁
    private boolean isDestroyed;

    @Override
    public void addListener(LifecycleListener listener) {
        lifecycleListeners.add(listener);
        if (isDestroyed) {
            listener.onDestroy();
        } else if (isStarted) {
            listener.onStart();
        } else {
            listener.onStop();
        }
    }

    @Override
    public void removeListener(LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    void onStart() {
        isStarted = true;
        for (LifecycleListener listener : lifecycleListeners) {
            listener.onStart();
        }
    }

    void onStop() {
        isStarted = false;
        for (LifecycleListener listener : lifecycleListeners) {
            listener.onStop();
        }
    }

    void onDestroy() {
        isDestroyed = false;
        for (LifecycleListener listener : lifecycleListeners) {
            listener.onDestroy();
        }
    }
}
