package com.karashok.demookhttp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class Dispatcher {

    //最多同时请求
    private int maxRequests;

    //同一个host同时最多请求
    private int maxRequestPerHost;

    //线程池，发送异步请求
    private ExecutorService executorService;

    /**
     * 等待执行队列
     */
    private final Deque<Call.AsyncCall> readAsyncCalls = new ArrayDeque<>();

    /**
     * 正在执行队列
     */
    private final Deque<Call.AsyncCall> runningAsyncCalls = new ArrayDeque<>();

    public Dispatcher() {
        this(64, 2);
    }

    public Dispatcher(int maxRequests, int maxRequestPerHost) {
        this.maxRequests = maxRequests;
        this.maxRequestPerHost = maxRequestPerHost;
    }

    /**
     * 线程池
     *
     * @return
     */
    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            ThreadFactory threadFactory = new ThreadFactory() {
                @Override
                public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread(runnable,"DemoDispatcher");
                    return thread;
                }
            };

            /**
             *    1、corePoolSize：线程池中核心线程数的最大值
             *    2、maximumPoolSize：线程池中能拥有最多线程数
             *    3、keepAliveTime：表示空闲线程的存活时间  60秒
             *    4、表示keepAliveTime的单位。
             *    5、workQueue：它决定了缓存任务的排队策略。
             *      SynchronousQueue<Runnable>：此队列中不缓存任何一个任务。向线程池提交任务时，
             *      如果没有空闲线程来运行任务，则入列操作会阻塞。当有线程来获取任务时，
             *      出列操作会唤醒执行入列操作的线程。
             *    6、指定创建线程的工厂
             */
            executorService = new ThreadPoolExecutor(0,Integer.MAX_VALUE,60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), threadFactory);
        }
        return executorService;
    }

    public void enqueue(Call.AsyncCall call) {
        if (runningAsyncCalls.size() < maxRequests &&
        runningCallsForHost(call) < maxRequestPerHost) {
            runningAsyncCalls.add(call);
            executorService().execute(call);
        } else {
            readAsyncCalls.add(call);
        }
    }

    /**
     * 判断是否执行等待队列中的请求
     */
    private void promoteCalls() {
        //同时请求达到上限
        if (runningAsyncCalls.size() >= maxRequests) {
            return;
        }
        //没有等待执行请求
        if (readAsyncCalls.isEmpty()) {
            return;
        }
        Iterator<Call.AsyncCall> iterator = readAsyncCalls.iterator();
        while (iterator.hasNext()) {
            Call.AsyncCall asyncCall = iterator.next();
            //同一host同时请求为达上限
            if (runningCallsForHost(asyncCall) < maxRequestPerHost) {
                readAsyncCalls.remove(asyncCall);
                runningAsyncCalls.add(asyncCall);
            }
            //到达同时请求上限
            if (runningAsyncCalls.size() >= maxRequests) {
                return;
            }
        }
    }

    /*
     *请求结束 移出正在运行队列
     *并判断是否执行等待队列中的请求
     */
    public void finished(Call.AsyncCall call) {
        synchronized (this) {
            runningAsyncCalls.remove(call);
            //判断是否执行等待队列中的请求
            promoteCalls();
        }
    }

    /**
     * 同一host 的 同时请求数
     *
     * @param call
     * @return
     */
    private int runningCallsForHost(Call.AsyncCall call) {
        int result = 0;
        for (Call.AsyncCall c : runningAsyncCalls) {
            //如果执行这个请求，则相同的host数量是result
            if (Objects.equals(c.host(),call.host())) {
                result++;
            }
        }
        return result;
    }
}
