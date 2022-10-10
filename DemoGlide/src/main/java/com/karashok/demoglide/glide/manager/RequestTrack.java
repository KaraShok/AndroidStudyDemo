package com.karashok.demoglide.glide.manager;

import com.karashok.demoglide.glide.GlideUtils;
import com.karashok.demoglide.glide.request.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class RequestTrack {

    private Set<Request> requests = Collections.newSetFromMap(new WeakHashMap<Request, Boolean>());

    /**
     * 如果停止了请求，则Request不在执行,Request只有弱引用 可能会被回收
     * 如果继续了请求
     * 防止停止掉的请求被回收
     */
    private List<Request> pendingRequests = new ArrayList<>();

    private boolean isPaused;

    /**
     * 执行请求
     */
    public void runRequest(Request request) {
        // 将请求加入一个集合当中 进行管理
        requests.add(request);
        if (!isPaused) {
            // 开始请求
            request.begin();
        } else {
            pendingRequests.add(request);
        }
    }

    /**
     * 暂停请求
     */
    public void pauseRequests() {
        isPaused = true;
        for (Request request : requests) {
            if (request.isRunning()) {
                request.pause();
                pendingRequests.add(request);
            }
        }
    }

    /**
     * 继续请求
     */
    public void resumeRequests() {
        isPaused = false;
        for (Request request : requests) {
            if (!request.isComplete() && !request.isCancelled() && !request.isRunning()) {
                request.begin();
            }
        }
        pendingRequests.clear();
    }

    /**
     * 清理请求
     */
    public void clearRequests() {
        for (Request request : requests) {
            if (request == null) {
                continue;
            }
            request.clear();
            request.recycle();
        }
        requests.clear();
        pendingRequests.clear();
    }

}
