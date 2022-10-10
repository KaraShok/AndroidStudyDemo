package com.karashok.demohermes;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-15-2022
 */
public class HermesInvocationHandler implements InvocationHandler {

    private Class aClass;
    private Class hermesService;

    private Gson gson = new Gson();

    public HermesInvocationHandler(Class hermesService, Class aClass) {
        this.aClass = aClass;
        this.hermesService = hermesService;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Response responce = Hermes.getInstance().sendObjectRequest(hermesService, aClass, method,
                objects);
        Log.d("DemoHermes:invoke", "invoke: " + responce.toString());
        if (!TextUtils.isEmpty(responce.data)) {
            ResponseBean responseBean = gson.fromJson(responce.data, ResponseBean.class);
//            Log.d("DemoHermes:invoke", "invoke: " + responceBean.toString());
            if (responseBean.data != null) {
                String data = gson.toJson(responseBean.data);
                Object fromJson = gson.fromJson(data, method.getReturnType());
//                Log.d("DemoHermes:invoke", "invoke: " + fromJson.hashCode() + fromJson.getClass().getName());
                return fromJson;
            }
        }
        return null;
    }
}
