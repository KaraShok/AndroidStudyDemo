package com.karashok.demohermes;

import android.util.Log;

import com.google.gson.Gson;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-15-2022
 */
public abstract class ResponceMake {

    protected Class<?> resultClass;
    protected Object[] mParameters;

    protected Gson gson = new Gson();

    protected TypeCenter typeCenter = TypeCenter.getInstance();

    protected static final ObjectCenter objectCenter = ObjectCenter.getInstance();

    protected abstract Object invokeMethod();

    protected abstract void setMethod(RequestBean requestBean);

    public Response makeResponce(Request request) {
        RequestBean requestBean = gson.fromJson(request.data,RequestBean.class);
        resultClass = typeCenter.getClassType(requestBean.resultClassName);
        RequestParameter[] parameters = requestBean.requestParameter;
        if (parameters != null && parameters.length > 0) {
            mParameters = new Object[parameters.length];
            for (int i = 0, length = parameters.length; i < length; i++) {
                RequestParameter para = parameters[i];
                Class<?> classType = typeCenter.getClassType(para.parameterClassName);
                mParameters[i] = gson.fromJson(para.parameterValue,classType);
            }
        } else {
            mParameters = new Object[0];
        }

        setMethod(requestBean);
        Object o = invokeMethod();
        if (o != null) {
            Log.d("DemoHermes:makeResponce", "makeResponce: " + o.toString());
        }
        ResponseBean bean = new ResponseBean(o);
        String toJson = gson.toJson(bean);
        Response responce = new Response(toJson);
        return responce;
    }
}
