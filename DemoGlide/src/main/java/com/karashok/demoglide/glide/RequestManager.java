package com.karashok.demoglide.glide;

import com.karashok.demoglide.glide.manager.Lifecycle;
import com.karashok.demoglide.glide.manager.LifecycleListener;
import com.karashok.demoglide.glide.manager.RequestTrack;
import com.karashok.demoglide.glide.request.Request;

import java.io.File;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-19-2022
 */
public class RequestManager implements LifecycleListener {

    private final Lifecycle lifecycle;
    private final GlideContext glideContext;
    RequestTrack requestTrack;

    public RequestManager(Lifecycle lifecycle, GlideContext glideContext) {
        this.lifecycle = lifecycle;
        this.glideContext = glideContext;
        requestTrack = new RequestTrack();

        // 注册生命周期回调监听
        lifecycle.addListener(this);
    }

    @Override
    public void onStart() {
        // 继续请求
        resumeRequests();
    }

    @Override
    public void onStop() {
        // 停止所有请求
        pauseRequests();
    }

    @Override
    public void onDestroy() {
        lifecycle.removeListener(this);
        requestTrack.clearRequests();
    }

    public void pauseRequests() {
        requestTrack.pauseRequests();
    }

    public void resumeRequests() {
        requestTrack.resumeRequests();
    }

    public RequestBuilder load(String url) {
        return new RequestBuilder(glideContext,this).load(url);
    }

    public RequestBuilder load(File file) {
        return new RequestBuilder(glideContext,this).load(file);
    }

    /**
     * 管理Request
     * 执行请求
     */
    public void track(Request request) {
        requestTrack.runRequest(request);
    }
}
