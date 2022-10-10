package com.karashok.demohermes;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-16-2022
 */
public class InstanceResponceMake extends ResponceMake{

    private Method mMethod;

    @Override
    protected Object invokeMethod() {
        try{
            Object invoke = mMethod.invoke(null, mParameters);
            Log.d("DemoHermes:invokeMethod", "invokeMethod: " + mMethod.getName());
            objectCenter.putObject(invoke.getClass().getName(),invoke);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void setMethod(RequestBean requestBean) {
        RequestParameter[] parameters = requestBean.requestParameter;
        Class<?>[] parameterTypes = null;
        if (parameters != null && parameters.length > 0) {
            parameterTypes = new Class[parameters.length];
            for (int i = 0, length = parameters.length; i < length; i++) {
                parameterTypes[i] = typeCenter.getClassType(parameters[i].parameterClassName);
            }
        }
        mMethod = TypeUtils.getMethodForGettingInstance(resultClass,requestBean.methodName,parameterTypes);
    }
}
