package com.karashok.demoaop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-24-2022
 */
public class MyInvocationHandler implements InvocationHandler {

    private Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        boolean checkLogin = true;
        if (checkLogin) {
            // 已经登录正常跳转
            return method.invoke(target,objects);
        } else {
            // 跳转登录
            return null;
        }
    }
}
