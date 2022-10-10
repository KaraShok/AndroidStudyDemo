package com.karashok.demoglide.glide.cache;

import android.content.ComponentCallbacks2;
import android.util.LruCache;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-11-2022
 */
public class LruArrayPool implements ArrayPool{

    public static final int ARRAY_POOL_SIZE_BYTES = 4 * 1024 * 1024;

    // 单个资源的与 maxsize 最大比例
    private static final int SINGLE_ARRAY_MAX_SIZE_DIVISOR = 2;

    // 溢出大小
    private static final int MAX_OVER_SIZE_MULTIPLE = 8;

    private final int maxSize;
    private LruCache<Integer,byte[]> cache;
    private final NavigableMap<Integer, Integer> sortedSizes = new TreeMap<>();


    public LruArrayPool() {
        this(ARRAY_POOL_SIZE_BYTES);
    }

    public LruArrayPool(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LruCache<Integer, byte[]>(maxSize) {

            @Override
            protected int sizeOf(Integer key, byte[] value) {
                return value.length;
            }

            @Override
            protected void entryRemoved(boolean evicted, Integer key, byte[] oldValue,
                                        byte[] newValue) {
                sortedSizes.remove(oldValue.length);
            }
        };
    }

    @Override
    public byte[] get(int len) {
        // 获得等于或大于比 len 大的 key
        Integer key = sortedSizes.ceilingKey(len);
        if (key != null) {
            // 缓存中的大小只能比需要的大小溢出 8 倍
            if (key <= (MAX_OVER_SIZE_MULTIPLE * len)) {
                byte[] bytes = cache.remove(key);
                sortedSizes.remove(key);
                return bytes == null ? new byte[len] : bytes;
            }
        }
        return new byte[len];
    }

    @Override
    public void put(byte[] data) {
        int length = data.length;
        // 太大了，不缓存
        if (!isSmallEnougthForReuse(length)) {
            return;
        }
        sortedSizes.put(length,1);
        cache.put(length,data);
    }

    private boolean isSmallEnougthForReuse(int byteSize) {
        return byteSize <= maxSize / SINGLE_ARRAY_MAX_SIZE_DIVISOR;
    }

    @Override
    public void clearMemory() {
        cache.evictAll();
    }

    @Override
    public void trimMemory(int level) {
        if (level >= ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            clearMemory();
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN ||
            level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
            cache.trimToSize(maxSize / 2);
        }
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }
}
