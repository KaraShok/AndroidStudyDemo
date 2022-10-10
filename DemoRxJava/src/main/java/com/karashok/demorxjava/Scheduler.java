package com.karashok.demorxjava;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * 线程调度器
 */
public abstract class Scheduler {

    /**
     * IO 线程调度器
     */
    public static final Scheduler IO = new SchedulerIO();

    /**
     * Android 主线程调度器
     */
    public static final Scheduler AndroidMain = new SchedulerAndroidMain();

    /**
     * 线程切换
     * @param runnable
     */
    public abstract void scheduleDirect(Runnable runnable);

    /**
     * 线程清理
     */
    public abstract void close();
}
