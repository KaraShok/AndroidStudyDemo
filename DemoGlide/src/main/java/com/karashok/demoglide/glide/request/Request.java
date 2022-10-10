package com.karashok.demoglide.glide.request;

import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.karashok.demoglide.glide.GlideContext;
import com.karashok.demoglide.glide.GlideUtils;
import com.karashok.demoglide.glide.Target;
import com.karashok.demoglide.glide.cache.recycle.Resource;
import com.karashok.demoglide.glide.load.Engine;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class Request implements Target.SizeReadyCallback, ResourceCallback {

    private enum Status {
        PENDING,
        RUNNING,
        WAITING_FOR_SIZE,
        COMPLETE,
        FAILED,
        CANCELLED,
        CLEARED,
        PAUSED,
    }

    private Engine engine;
    private GlideContext glideContext;
    private Object model;
    private RequestOptions requestOptions;
    private Target target;
    private Resource resource;
    private Engine.LoadStatus loadStatus;
    private Status status;
    private Drawable errorDrawable;
    private Drawable placeHolderDrawable;

    public Request(GlideContext glideContext, Object model, RequestOptions requestOptions,
                   Target target) {
        this.glideContext = glideContext;
        this.model = model;
        this.requestOptions = requestOptions;
        this.target = target;
        this.engine = glideContext.getEngine();
        status = Status.PENDING;
    }

    public void recycle() {
        glideContext = null;
        model = null;
        requestOptions = null;
        target = null;
        loadStatus = null;
        errorDrawable = null;
        placeHolderDrawable = null;
    }

    public void begin() {

        // 设置状态为 等待大小的获得
        status = Status.WAITING_FOR_SIZE;

        // 开始加载 先设置占位图片
        target.onLoadStarted(getPlaceHolderDrawable());

        // requestOptions 占位图 失败图 固定大小的配置
        // 固定大小宽高是否有效
        if (requestOptions.getOverrideWidth() > 0 && requestOptions.getOverrideHeight() > 0) {
            onSizeReady(requestOptions.getOverrideWidth(),requestOptions.getOverrideHeight());
        } else {

            // TODO 计算View 大小
            // 否则计算size 计算完成后会回调 onSizeReady
            target.getSize(this);
        }
    }

    /**
     * 取消
     */
    public void cancel() {
        target.cancel();
        status = Status.CANCELLED;
        if (loadStatus != null) {
            loadStatus.cancel();
            loadStatus = null;
        }
    }

    public void clear() {
        if (status == Status.CLEARED) {
            return;
        }
        cancel();
        if (resource != null) {
            releaseResource(resource);
        }
        status = Status.CLEARED;
    }

    public boolean isPaused() {
        return status == Status.PAUSED;
    }

    public void pause() {
        clear();
        status = Status.PAUSED;
    }

    private void releaseResource(Resource resource) {
        resource.release();
        this.resource = null;
    }

    public boolean isRunning() {
        return status == Status.RUNNING || status == Status.WAITING_FOR_SIZE;
    }

    public boolean isComplete() {
       return status == Status.COMPLETE;
    }

    public boolean isCancelled() {
        return status == Status.CANCELLED || status == Status.CLEARED;
    }

    private Drawable getErrorDrawable() {
        if (errorDrawable == null && requestOptions.getErrorId() > 0) {
            errorDrawable = loadDrawable(requestOptions.getErrorId());
        }
        return errorDrawable;
    }

    private Drawable getPlaceHolderDrawable() {
        if (placeHolderDrawable == null && requestOptions.getPlaceHolderId() > 0) {
            placeHolderDrawable = loadDrawable(requestOptions.getPlaceHolderId());
        }
        return placeHolderDrawable;
    }

    private Drawable loadDrawable(int resId) {
        return ResourcesCompat.getDrawable(glideContext.getApplicationContext().getResources(),
                resId,
                glideContext.getApplicationContext().getTheme());
    }

    private void setErrorDrawable() {
        Drawable error = getErrorDrawable();
        if (error == null) {
            error = getPlaceHolderDrawable();
        }
        target.onLoadFailed(error);
    }

    @Override
    public void onSizeReady(int width, int height) {

        //是否处于等待大小状态 如果不等于 可能状态混乱了
//        if (status != Status.WAITING_FOR_SIZE) {
//            return;
//        }

        // 运行状态  加载状态
        status = Status.RUNNING;

        // 加载图片
        loadStatus = engine.load(glideContext,model,width,height,this);
    }

    @Override
    public void onResourceSuccess(Resource resource) {
        loadStatus = null;
        this.resource = resource;
        if (resource == null) {
            status = Status.FAILED;
            setErrorDrawable();
            return;
        }
        target.onResourceReady(resource.getBitmap());
    }
}
