package com.karashok.demoioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-23-2022
 */
public class EventInvocationHandler implements InvocationHandler {

    /**
     * 持有方法的类
     */
    private Object context;

    /**
     * 要执行的方法
     */
    private Method contextMethod;

    public EventInvocationHandler(Object context, Method method) {
        this.context = context;
        this.contextMethod = method;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return contextMethod.invoke(context,objects);
    }
}
