package com.karashok.demoeventbus;

import java.lang.reflect.Method;

/**
 * @author KaraShokZ.
 * @des 存储订阅信息
 * @since 05-08-2022
 */
public class DemoSubscribleMethod {

    /**
     * 订阅的方法
     */
    private Method method;

    /**
     * 订阅的事件
     */
    private Class<?> eventType;

    /**
     * 线程模型
     */
    private DemoThreadMode threadMode;

    /**
     * 订阅者
     */
    private Object subscriber;

    public DemoSubscribleMethod(Method method, Class<?> eventType, DemoThreadMode threadMode,
                                Object subscriber) {
        this.method = method;
        this.eventType = eventType;
        this.threadMode = threadMode;
        this.subscriber = subscriber;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getEventType() {
        return eventType;
    }

    public void setEventType(Class<?> eventType) {
        this.eventType = eventType;
    }

    public DemoThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(DemoThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Object getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Object subscriber) {
        this.subscriber = subscriber;
    }
}
