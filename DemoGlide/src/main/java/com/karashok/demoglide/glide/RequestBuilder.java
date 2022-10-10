package com.karashok.demoglide.glide;

import android.widget.ImageView;

import com.karashok.demoglide.glide.request.Request;
import com.karashok.demoglide.glide.request.RequestOptions;

import java.io.File;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-21-2022
 */
public class RequestBuilder {

    private final GlideContext glideContext;
    private RequestOptions requestOptions;
    private RequestManager requestManager;
    private Object model;

    public RequestBuilder(GlideContext glideContext, RequestManager requestManager) {
        this.glideContext = glideContext;
        this.requestOptions = glideContext.defaultRequestOptions;
        this.requestManager = requestManager;
    }

    public RequestBuilder apply(RequestOptions requestOptions) {
        this.requestOptions = requestOptions;
        return this;
    }

    public RequestBuilder load(String url) {
        model = url;
        return this;
    }

    public RequestBuilder load(File file) {
        model = file;
        return this;
    }

    /**
     * 加载图片并设置到ImageView当中
     *
     * @param view
     */
    public void into(ImageView view) {

        // 将 View 交给 Target
        Target target = new Target(view);

        // 图片加载与设置
        Request request = new Request(glideContext,model,requestOptions,target);

        // Request 交给 RequestManager 管理
        requestManager.track(request);
    }
}
