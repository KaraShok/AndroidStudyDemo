package com.karashok.demoglide.glide.cache;

import com.karashok.demoglide.glide.cache.recycle.Resource;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author KaraShokZ.
 * @des 正在使用的图片资源
 * @since 07-03-2022
 */
public class ActiveResources {

    private ReferenceQueue<Resource> queue;
    private final Resource.ResourceListener resourceListener;
    private Map<Key, ResourceWeakReference> activateResources = new HashMap<>();
    private Thread cleanReferenceQueueThread;
    private boolean isShutDown;

    public ActiveResources(Resource.ResourceListener resourceListener) {
        this.resourceListener = resourceListener;
    }

    /**
     * 加入活动缓存
     *
     * @param key
     * @param resource
     */
    public void activate(Key key, Resource resource) {
        resource.setResourceListener(key,resourceListener);
        activateResources.put(key, new ResourceWeakReference(key,resource,getReferenceQueue()));
    }

    /**
     * 移除活动缓存
     */
    public Resource deactivate(Key key) {
        ResourceWeakReference reference = activateResources.remove(key);
        if (reference != null) {
            return reference.get();
        }
        return null;
    }

    /**
     * 获得对应value
     * @param key
     * @return
     */
    public Resource get(Key key) {
        ResourceWeakReference reference = activateResources.get(key);
        if (reference != null) {
            return reference.get();
        }
        return null;
    }

    public void shutDown() {
        isShutDown = true;
        if (cleanReferenceQueueThread != null) {
            cleanReferenceQueueThread.interrupt();
            try{
                cleanReferenceQueueThread.join(TimeUnit.SECONDS.toMillis(5));
                if (cleanReferenceQueueThread.isAlive()) {
                    throw new RuntimeException("Failed to join in time");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 引用队列，通知我们弱引用被回收了
     * 让我们得到通知的作用
     *
     * @return
     */
    private ReferenceQueue<Resource> getReferenceQueue() {
        if (queue == null) {
            queue = new ReferenceQueue<>();
            cleanReferenceQueueThread = new Thread() {
                @Override
                public void run() {
                    while (!isShutDown) {
                        try{
                            ResourceWeakReference ref = (ResourceWeakReference) queue.remove();
                            activateResources.remove(ref.key);
                        } catch (Exception e) {

                        }
                    }
                }
            };
            cleanReferenceQueueThread.start();
        }
        return queue;
    }
    static final class ResourceWeakReference extends WeakReference<Resource> {

        final Key key;

        public ResourceWeakReference(Key key, Resource referent, ReferenceQueue<? super Resource> q) {
            super(referent, q);
            this.key = key;
        }
    }
}
