package com.karashok.demohermes;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-15-2022
 */
public class Hermes {

    private static final Hermes instance = new Hermes();

    private ServiceConnectionManager serviceConnectionManager = ServiceConnectionManager.getInstance();
    private TypeCenter typeCenter = TypeCenter.getInstance();
    private Gson gson = new Gson();

    public Hermes() {
    }

    public static Hermes getInstance() {
        return instance;
    }

    /**
     * 注册服务
     * @param cls
     */
    public void register(Class<?> cls) {
        typeCenter.register(cls);
    }

    /**
     * 连接服务
     * @param context
     * @param hermesServiceClass
     */
    public void connect(Context context, Class<HermesService> hermesServiceClass) {
        connectApp(context,null,hermesServiceClass);
    }

    private void connectApp(Context context,String packageName, Class<HermesService> hermesServiceClass) {
        serviceConnectionManager.bind(context.getApplicationContext(),packageName,hermesServiceClass);
    }

    /**
     * 获取远程服务单例对象
     * @param tClass
     * @param parameters
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> tClass, Object... parameters) {
        Response response = sendRequest(HermesService.class, tClass, null, parameters);
        return getProxy(HermesService.class,tClass);
    }

    private <T> T getProxy(Class<HermesService> hermesServiceClass,Class<T> tClass) {
        ClassLoader classLoader = hermesServiceClass.getClassLoader();
        Log.d("DemoHermes", "getProxy: " + tClass.getName());
        T proxy = (T) Proxy.newProxyInstance(classLoader,new Class<?>[]{tClass},new HermesInvocationHandler(hermesServiceClass,tClass));
        Log.d("DemoHermes", "getProxy: " + proxy.hashCode());
        return proxy;
    }

    private <T> Response sendRequest(Class<HermesService> hermesServiceClass, Class<T> tClass, Method method, Object[] parameters) {
        RequestBean requestBean = new RequestBean();
        String className = null;
        if (tClass.getAnnotation(ClassId.class) == null) {
            String tClassName = tClass.getName();
            requestBean.className = tClassName;
            requestBean.resultClassName = tClassName;
        } else {
            String value = tClass.getAnnotation(ClassId.class).value();
            requestBean.className = value;
            requestBean.resultClassName = value;
        }

        if (method != null) {
            requestBean.methodName = TypeUtils.getMethodId(method);
        }

        RequestParameter[] requestParameters = null;
        if (parameters != null && parameters.length > 0) {
            for (int i = 0, length = parameters.length; i < length; i++) {
                Object obj = parameters[i];
                requestParameters[i] = new RequestParameter(obj.getClass().getName(),gson.toJson(obj));
            }
        }

        if (requestParameters != null) {
            requestBean.requestParameter = requestParameters;
        }
        Response response = serviceConnectionManager.request(hermesServiceClass,new Request(gson.toJson(requestBean),Request.TYPE_GET));
        Log.d("DemoHermes", "sendRequest: " + response.toString());
        return response;
    }

    public <T> Response sendObjectRequest(Class hermesServiceClass, Class<?> aClass, Method method, Object[] args) {
        RequestBean requestBean = new RequestBean();
        String className = null;
        if (aClass.getAnnotation(ClassId.class) == null) {
            String tClassName = aClass.getName();
            requestBean.className = tClassName;
            requestBean.resultClassName = tClassName;
        } else {
            String value = aClass.getAnnotation(ClassId.class).value();
            requestBean.className = value;
            requestBean.resultClassName = value;
        }

        if (method != null) {
            requestBean.methodName = TypeUtils.getMethodId(method);
        }

        RequestParameter[] requestParameters = null;
        if (args != null && args.length > 0) {
            requestParameters = new RequestParameter[args.length];
            for (int i = 0, length = args.length; i < length; i++) {
                Object obj = args[i];
                requestParameters[i] = new RequestParameter(obj.getClass().getName(),gson.toJson(obj));
            }
        }

        if (requestParameters != null) {
            requestBean.requestParameter = requestParameters;
        }

        return serviceConnectionManager.request(hermesServiceClass, new Request(gson.toJson(requestBean),Request.TYPE_NEW));
    }
}
