package com.karashok.demoglide.glide.load;

import com.karashok.demoglide.glide.cache.Key;
import com.karashok.demoglide.glide.cache.recycle.Resource;
import com.karashok.demoglide.glide.request.ResourceCallback;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class EngineJob implements DecodeJob.Callback{

    private static final String TAG = "EngineJob";

    private static final int MSG_COMPLETE = 1;
    private static final int MSG_EXCEPTION = 2;
    private static final int MSG_CANCELLED = 3;

    private static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper(), new MainThreadCallback());

    private Resource resource;
    private EngineKey key;
    private final List<ResourceCallback> cbs = new ArrayList<>();
    private final ThreadPoolExecutor threadPool;
    private final EngineJobListener listener;
    private boolean isCancelled;
    private DecodeJob decodeJob;

    public EngineJob(EngineKey key, ThreadPoolExecutor threadPool, EngineJobListener listener) {
        this.key = key;
        this.threadPool = threadPool;
        this.listener = listener;
    }

    public void addCallback(ResourceCallback cb) {
        Log.d(TAG, "addCallback: 设置加载状态监听");
        cbs.add(cb);
    }

    public void removeCallback(ResourceCallback cb) {
        Log.d(TAG, "removeCallback: 移除加载状态监听");
        cbs.remove(cb);

        // 这一个请求取消了，可能还有其他地方的请求
        // 只有回调为空 才表示请求需要取消
        if (cbs.isEmpty()) {
            cancel();
        }
    }

    void cancel() {
        isCancelled = true;
        decodeJob.cancel();
        listener.onEngineJobCancelled(this, key);
    }

    public void start(DecodeJob decodeJob) {
        Log.d(TAG, "start: 开始加载工作");
        this.decodeJob = decodeJob;
        threadPool.execute(decodeJob);
    }

    @Override
    public void onResourceSuccess(Resource resource) {
        this.resource = resource;
        MAIN_THREAD_HANDLER.obtainMessage(MSG_COMPLETE,this).sendToTarget();
    }

    @Override
    public void onLoadFailed(Throwable e) {
        MAIN_THREAD_HANDLER.obtainMessage(MSG_EXCEPTION,this).sendToTarget();
    }

    private void handleCancelledOnMainThread() {
        listener.onEngineJobCancelled(this,key);
        release();
    }

    /**
     * 图片加载成功
     */
    private void handleResultOnMainThread() {
        if (isCancelled) {
            resource.recycle();
            release();
            return;
        }

        // 将引用计数+1
        resource.acquire();

        // 1、回调给Engine 操作缓存
        listener.onEngineJobComplete(this,key,resource);

        // 2、cbs里面是ResourceCallback集合，这里其实就是一堆的Request
        for (int i = 0, size = cbs.size(); i < size; i++) {
            ResourceCallback callback = cbs.get(i);

            // 引用计数 +1
            resource.acquire();
            callback.onResourceSuccess(resource);
        }

        // 将引用计数-1
        resource.release();
        release();
    }

    private void handleExceptionOnMainThread() {
        if (isCancelled) {
            release();
            return;
        }
        listener.onEngineJobComplete(this,key,null);
        for (ResourceCallback cb : cbs) {
            cb.onResourceSuccess(null);
        }
    }

    private void release() {
        cbs.clear();
        key = null;
        resource = null;
        isCancelled = false;
        decodeJob = null;
    }

    public interface EngineJobListener {

        void onEngineJobComplete(EngineJob engineJob, Key key, Resource resource);

        void onEngineJobCancelled(EngineJob engineJob, Key key);
    }

    private static class MainThreadCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            EngineJob job = (EngineJob) msg.obj;
            switch (msg.what) {
                case MSG_COMPLETE:
                    job.handleResultOnMainThread();
                    break;
                case MSG_EXCEPTION:
                    job.handleExceptionOnMainThread();
                    break;
                case MSG_CANCELLED:
                    job.handleCancelledOnMainThread();
                    break;
                default:
                    throw new IllegalStateException("Unrecognized message: " + msg.what);
            }
            return true;
        }
    }
}
