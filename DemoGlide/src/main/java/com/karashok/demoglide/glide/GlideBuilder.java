package com.karashok.demoglide.glide;

import android.app.ActivityManager;
import android.content.Context;
import android.util.DisplayMetrics;

import com.karashok.demoglide.glide.cache.ArrayPool;
import com.karashok.demoglide.glide.cache.DiskCache;
import com.karashok.demoglide.glide.cache.DiskLruCacheWrapper;
import com.karashok.demoglide.glide.cache.LruArrayPool;
import com.karashok.demoglide.glide.cache.LruMemoryCache;
import com.karashok.demoglide.glide.cache.MemoryCache;
import com.karashok.demoglide.glide.cache.recycle.BitmapPool;
import com.karashok.demoglide.glide.cache.recycle.LruBitmapPool;
import com.karashok.demoglide.glide.load.Engine;
import com.karashok.demoglide.glide.load.GlideExecutor;
import com.karashok.demoglide.glide.request.RequestOptions;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-21-2022
 */
public class GlideBuilder {

    MemoryCache memoryCache;
    DiskCache diskCache;
    BitmapPool bitmapPool;

    // 进行数组的缓存
    ArrayPool arrayPool;
    RequestOptions defaultRequestOptions = new RequestOptions();
    ThreadPoolExecutor executor;
    Engine engine;

    public void setMemoryCache(MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
    }

    public void setDiskCache(DiskCache diskCache) {
        this.diskCache = diskCache;
    }

    public void setBitmapPool(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }

    public void setArrayPool(ArrayPool arrayPool) {
        this.arrayPool = arrayPool;
    }

    public void setDefaultRequestOptions(RequestOptions defaultRequestOptions) {
        this.defaultRequestOptions = defaultRequestOptions;
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    private static int getMaxSize(ActivityManager activityManager) {
        // 使用最大可用内存的0.4作为缓存使用  64M
        final int memoryClassBytes = activityManager.getMemoryClass() * 1024 *1024;
        return Math.round(memoryClassBytes * 0.4f);
    }

    public Glide build(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context
                .ACTIVITY_SERVICE);

        // Glide缓存最大可用内存大小
        int maxSize = getMaxSize(activityManager);
        if (arrayPool == null) {
            arrayPool = new LruArrayPool();
        }

        // 减去数组缓存后的可用内存大小
        int availableSize = maxSize - arrayPool.getMaxSize();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;

        // 获得一个屏幕大小的argb所占的内存大小
        int screenSize = widthPixels * heightPixels * 4;

        // bitmap 复用占 4份
        float bitmapPoolSize = screenSize * 4.0f;

        // 内存缓存占 2份
        float memoryCacheSize = screenSize * 2.0f;

        if (bitmapPoolSize + memoryCacheSize <= availableSize) {
            bitmapPoolSize = Math.round(bitmapPoolSize);
            memoryCacheSize = Math.round(memoryCacheSize);
        } else {
            // 把总内存分成 6分
            float part = availableSize / 6.0f;
            bitmapPoolSize = Math.round(part * 4);
            memoryCacheSize = Math.round(part * 2);
        }

        if (bitmapPool == null) {
            bitmapPool = new LruBitmapPool((int) bitmapPoolSize);
        }
        if (memoryCache == null) {
            memoryCache = new LruMemoryCache((int) memoryCacheSize);
        }
        if (diskCache == null) {
            diskCache = new DiskLruCacheWrapper(context);
        }
        if (executor == null) {
            executor = GlideExecutor.newExecutor();
        }

        engine = new Engine(diskCache, bitmapPool, memoryCache, executor);
        memoryCache.setResourceRemoveListener(engine);
        return new Glide(context, this);
    }
}
