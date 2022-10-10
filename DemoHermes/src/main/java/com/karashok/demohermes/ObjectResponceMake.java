package com.karashok.demohermes;

import java.lang.reflect.Method;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-16-2022
 */
public class ObjectResponceMake extends ResponceMake{

    private Method mMethod;

    private Object mObject;

    @Override
    protected Object invokeMethod() {
        try{
            return mMethod.invoke(mObject,mParameters);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    protected void setMethod(RequestBean requestBean) {
        mObject = objectCenter.getObject(resultClass.getName());
        mMethod = typeCenter.getMethod(mObject.getClass(),requestBean);
    }
}
