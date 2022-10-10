package com.karashok.demohermes;

import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-15-2022
 */
public class TypeCenter {

    private final ConcurrentHashMap<Class<?>,ConcurrentHashMap<String, Method>> mMethodsMap;
    private final ConcurrentHashMap<String,Class<?>> mClassMap;

    private static final TypeCenter instance = new TypeCenter();

    public TypeCenter() {
        this.mMethodsMap = new ConcurrentHashMap<>();
        this.mClassMap = new ConcurrentHashMap<>();
    }

    public static final TypeCenter getInstance() {
        return instance;
    }

    /**
     * 注册服务类
     * @param cls
     */
    public void register(Class<?> cls) {
        registerClass(cls);
        registerMethod(cls);
    }

    /**
     * 缓存类
     * @param cls
     */
    private void registerClass(Class<?> cls) {
        String name = cls.getName();
        mClassMap.putIfAbsent(name,cls);
    }

    /**
     * 缓存类的方法
     * @param cls
     */
    private void registerMethod(Class<?> cls) {
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            mMethodsMap.putIfAbsent(cls,new ConcurrentHashMap<>());
            ConcurrentHashMap<String,Method> map = mMethodsMap.get(cls);
            String methodId = TypeUtils.getMethodId(method);
            map.put(methodId,method);
        }
    }

    public Class<?> getClassType(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        Class<?> cls = mClassMap.get(name);
        if (cls == null) {
            try {
                cls = Class.forName(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cls;
    }

    public Method getMethod(Class<?> aClass, RequestBean requestBean) {
        if (requestBean.methodName != null) {
            mMethodsMap.putIfAbsent(aClass,new ConcurrentHashMap<>());
            ConcurrentHashMap<String, Method> map = mMethodsMap.get(aClass);
            Method method = map.get(requestBean.methodName);
            if (method != null) {
                return method;
            }
            int pos = requestBean.methodName.indexOf("(");
            Class[] parameters = null;
            RequestParameter[] requestParameters = requestBean.requestParameter;
            if (requestParameters != null && requestParameters.length > 0) {
                parameters = new Class[requestParameters.length];
                for (int i = 0, length = requestParameters.length; i < length; i++) {
                    parameters[i] = getClassType(requestParameters[i].parameterClassName);
                }
            }
            method = TypeUtils.getMethod(aClass, requestBean.methodName.substring(0,pos), parameters);
            map.put(requestBean.methodName,method);
            return method;
        }
        return null;
    }
}
