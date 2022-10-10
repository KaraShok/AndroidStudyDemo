package com.karashok.demoglide.glide.cache;

import android.content.ComponentCallbacks2;
import android.util.LruCache;

import com.karashok.demoglide.glide.cache.recycle.Resource;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-16-2022
 */
public class LruMemoryCache extends LruCache<Key, Resource> implements MemoryCache {

    private ResourceRemoveListener listener;
    private boolean isRemoved;

    public LruMemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(Key key, Resource value) {
        return value.getBitmap().getAllocationByteCount();
    }

    @Override
    protected void entryRemoved(boolean evicted, Key key, Resource oldValue, Resource newValue) {
        // 给复用池使用
        if (listener != null && oldValue != null && !isRemoved) {
            listener.onResourceRemoved(oldValue);
        }
    }

    @Override
    public void clearMemory() {
        evictAll();
    }

    @Override
    public void trimMemory(int level) {
        // 程序进入后台，进入加入 lru 列表，系统资源紧张时根据 lru 列表杀死进程
        if (level >= ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            clearMemory();
        // 程序所有 UI 被隐藏，应该释放一部分资源
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            trimToSize(maxSize() / 2);
        }
    }

    /**
     * 设置移除监听
     * @param listener
     */
    @Override
    public void setResourceRemoveListener(ResourceRemoveListener listener) {
        this.listener = listener;
    }

    @Override
    public Resource removeResource(Key key) {
        // 如果是主动移除的不会调 listener.onResourceRemoved
        isRemoved = true;
        Resource remove = remove(key);
        isRemoved = false;
        return remove;
    }
}
