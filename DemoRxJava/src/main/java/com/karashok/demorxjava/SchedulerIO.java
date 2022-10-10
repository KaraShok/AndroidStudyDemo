package com.karashok.demorxjava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * IO 线程调度器
 */
public class SchedulerIO extends Scheduler{

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void close() {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    @Override
    public void scheduleDirect(Runnable runnable) {
        executorService.submit(runnable);
    }
}
