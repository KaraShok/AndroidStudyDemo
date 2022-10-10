package com.karashok.demoglide.glide.load;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class GlideExecutor {

    private static int bestThreadCount;

    public static int calculateBestThreadCount() {
        if (bestThreadCount == 0) {
            bestThreadCount = Math.min(4, Runtime.getRuntime().availableProcessors());
        }
        return bestThreadCount;
    }

    public static ThreadPoolExecutor newExecutor() {
        int threadCount = calculateBestThreadCount();
        return new ThreadPoolExecutor(
                threadCount,
                threadCount,
                0,
                TimeUnit.MICROSECONDS,
                new LinkedBlockingDeque<Runnable>(),
                new DefaultThreadFactory()
        );
    }

    private static final class DefaultThreadFactory implements ThreadFactory {

        private int threadNum;

        @Override
        public Thread newThread(Runnable r) {
            final Thread thread = new Thread(r,"glide-thread-" + threadNum);
            threadNum++;
            return thread;
        }
    }
}
