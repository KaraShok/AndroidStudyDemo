package com.karashok.demorxjava;

import android.os.Handler;
import android.os.Looper;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * Android 主线程线程调度器
 */
public class SchedulerAndroidMain extends Scheduler{

    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    public void scheduleDirect(Runnable runnable) {
        mHandler.post(runnable);
    }

    @Override
    public void close() {

    }
}
