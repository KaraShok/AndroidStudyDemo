package com.karashok.demoeventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-08-2022
 */
public class DemoEventBus {


    private static final DemoEventBus instance = new DemoEventBus();

    /**
     * 根据订阅者缓存订阅方法
     */
    private Map<Object, List<DemoSubscribleMethod>> subscriptionsBySubscriber;

    private Handler handler;

    private ExecutorService executorService;

    private DemoEventBus() {
        handler = new Handler(Looper.getMainLooper());
        executorService = Executors.newCachedThreadPool();
        subscriptionsBySubscriber = new HashMap<>();
    }

    public static DemoEventBus getDefault() {
        return instance;
    }

    public void register(Object subscriber) {
        if (subscriptionsBySubscriber.get(subscriber) == null) {
            subscriptionsBySubscriber.put(subscriber,getSubscribleMethods(subscriber));
        }
    }

    private List<DemoSubscribleMethod> getSubscribleMethods(Object subscriber) {
        List<DemoSubscribleMethod> methods = new ArrayList<>();
        Class<?> aClass = subscriber.getClass();
        while (aClass != null) {
            String aClassName = aClass.getName();
            if (aClassName.startsWith("java.") ||
                    aClassName.startsWith("javax.") ||
                    aClassName.startsWith("android.") ||
                    aClassName.startsWith("androidx.")) {
                break;
            }

            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(DemoSubscribe.class)) {
                    if (method.getParameterCount() != 1) {
                        throw new IllegalArgumentException("DemoEventBus 只接收一个参数， " + method.getName() + " 方法参数过多");
                    }
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    DemoSubscribe declaredAnnotation = method.getDeclaredAnnotation(DemoSubscribe.class);
                    DemoSubscribleMethod subscribleMethod = new DemoSubscribleMethod(method,
                            parameterTypes[0],
                            declaredAnnotation.threadMode(),
                            subscriber);
                    methods.add(subscribleMethod);

                }
            }
            aClass = aClass.getSuperclass();
        }
        return methods;
    }

    public void post(Object event) {
        if (subscriptionsBySubscriber.isEmpty()) {
            return;
        }
        Collection<List<DemoSubscribleMethod>> values = subscriptionsBySubscriber.values();
        Class<?> eventClass = event.getClass();
        for (List<DemoSubscribleMethod> methodList : values) {
            for (DemoSubscribleMethod subscribleMethod : methodList) {
                if (subscribleMethod.getEventType().isAssignableFrom(eventClass)) {
                    switch (subscribleMethod.getThreadMode()) {
                        case MAIN:
                            if (Looper.getMainLooper().isCurrentThread()) {
                                subscribleMethodInvoke(subscribleMethod,event);
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        subscribleMethodInvoke(subscribleMethod,event);
                                    }
                                });
                            }
                            break;
                        case ASYNC:
                            if (Looper.getMainLooper().isCurrentThread()) {
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        subscribleMethodInvoke(subscribleMethod,event);
                                    }
                                });
                            } else {
                                subscribleMethodInvoke(subscribleMethod,event);
                            }
                            break;
                        default:
                            subscribleMethodInvoke(subscribleMethod,event);
                            break;
                    }
                }
            }
        }
    }

    private void subscribleMethodInvoke(DemoSubscribleMethod subscribleMethod, Object event) {
        try{
            subscribleMethod.getMethod().invoke(subscribleMethod.getSubscriber(),event);
        } catch (IllegalAccessException e) {

        } catch (InvocationTargetException e) {

        }
    }

    public synchronized void unregister(Object subscriber) {
        List<DemoSubscribleMethod> subscribleMethodList = subscriptionsBySubscriber.get(subscriber);
        if (subscribleMethodList != null) {
            subscribleMethodList.clear();
            subscriptionsBySubscriber.remove(subscriber);
        }
    }
}
