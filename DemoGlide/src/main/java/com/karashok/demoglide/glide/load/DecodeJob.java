package com.karashok.demoglide.glide.load;

import android.graphics.Bitmap;
import android.util.Log;

import com.karashok.demoglide.glide.GlideContext;
import com.karashok.demoglide.glide.cache.DiskCache;
import com.karashok.demoglide.glide.cache.Key;
import com.karashok.demoglide.glide.cache.recycle.Resource;
import com.karashok.demoglide.glide.load.generator.DataCacheGenerator;
import com.karashok.demoglide.glide.load.generator.DataGenerator;
import com.karashok.demoglide.glide.load.generator.SourceGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class DecodeJob implements Runnable, DataGenerator.DataGeneratorCallBack {

    private static final String TAG = "DecodeJob";
    private final int width;
    private final int height;
    private final Callback callback;
    private final Object model;
    private final DiskCache diskCache;
    private final GlideContext glideContext;
    private DataGenerator currentGenerator;
    private boolean isCancelled;
    private boolean isCallbackNotified;
    private Key sourceKey;
    private Stage stage;

    public DecodeJob(int width, int height, Callback callback, Object model, DiskCache diskCache,
                     GlideContext glideContext) {
        this.width = width;
        this.height = height;
        this.callback = callback;
        this.model = model;
        this.diskCache = diskCache;
        this.glideContext = glideContext;
    }

    public void cancel() {
        isCancelled = true;
        if (currentGenerator != null) {
            currentGenerator.cancel();
        }
    }

    /**
     * 加载器加载完成后回调(目前只有InputStream类型数据)
     *
     * @param sourceKey
     * @param data      InputStream
     */
    @Override
    public void onDataReady(Key sourceKey, Object data, DataSource dataSource) {
        this.sourceKey = sourceKey;
        Log.d(TAG, "onDataReady: 加载成功,开始解码数据");
        runLoadPath(data,dataSource);
    }

    @Override
    public void onDataFetcherFailed(Key sourceKey, Exception e) {
        //再次运行 失败的话 状态变为finish则 结束
        Log.e(TAG, "加载失败，尝试使用下一个加载器:" + e.getMessage());
        runGenerators();
    }

    @Override
    public void run() {
        try{
            Log.d(TAG, "run: 开始加载数据");

            // 由对应的fragment生命周期取消
            if (isCancelled) {
                Log.d(TAG, "run: 取消加载数据");
                callback.onLoadFailed(new IOException("Canceled"));
                return;
            }

            // 阶段 这里获得的是 查找文件缓存阶段
            stage = getNextStage(Stage.INITIALIZE);

            // 下一个数据生成器
            currentGenerator = getNextGenerator();

            // 使用这个生成器
            runGenerators();
        } catch (Throwable t) {
            callback.onLoadFailed(t);
        }
    }

    private void runGenerators() {

        // 使用了对应的生成器开始加载了
        boolean isStarted = false;
        while (!isCancelled && currentGenerator != null && !isStarted) {
            isStarted = currentGenerator.startNext();

            // 生成器工作了 就break
            if (isStarted) {
                break;
            }

            // 获得下一个阶段
            stage = getNextStage(stage);
            if (stage == Stage.FINISHED) {
                Log.d(TAG, "runGenerators: 状态结束,没有加载器能够加载对应数据");
                break;
            }
            // 通过当前的阶段获得 下一个生成器
            currentGenerator = getNextGenerator();
        }
        if ((stage == Stage.FINISHED || isCancelled) && !isStarted) {
            notifyFailed();
        }
    }

    private void notifyFailed() {
        Log.d(TAG, "notifyFailed: 加载失败");
        if (!isCallbackNotified) {
            isCallbackNotified = true;
            callback.onLoadFailed(new RuntimeException("Failed to load resource"));
        }
    }

    /**
     * 成功咯
     * @param bitmap
     * @param dataSource
     */
    private void notifyComplete(final Bitmap bitmap, DataSource dataSource) {
        // bitmap的来源是来自于 原数据
        if (dataSource == DataSource.REMOTE) {
            // 写入文件缓存
            diskCache.put(sourceKey, new DiskCache.Writer() {
                @Override
                public boolean write(File file) {
                    FileOutputStream os = null;
                    try{
                        os = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,90,os);
                        return true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally{
                        if (os != null) {
                            try{
                                os.close();
                            } catch (Exception e) {

                            }
                        }
                    }
                    return false;
                }
            });
        }
        Resource resource = new Resource(bitmap);
        callback.onResourceSuccess(resource);
    }

    private DataGenerator getNextGenerator() {
        switch (stage) {
            case DATA_CACHE:
                Log.d(TAG, "getNextGenerator: 使用磁盘缓存加载器");
                return new DataCacheGenerator(glideContext,diskCache,model,this);
            case SOURCE:
                Log.d(TAG, "getNextGenerator: 使用源资源加载器");
                return new SourceGenerator(this,model,glideContext);
            case FINISHED:
                return null;
            default:
                throw new IllegalStateException("Unrecognized stage: " + stage);
        }
    }

    /**
     * 解码
     * @param data
     * @param dataSource
     * @param <Data>
     */
    private <Data> void runLoadPath(Data data, DataSource dataSource) {
        LoadPath<Data> loadPath = glideContext.getRegistry().getLoadPath((Class<Data>) data.getClass());
        Bitmap bitmap = loadPath.runLoad(data,width,height);
        if (bitmap != null) {
            Log.d(TAG, "runLoadPath: 解码成功回调");
            notifyComplete(bitmap,dataSource);
        } else {
            Log.d(TAG, "runLoadPath: 解码失败，尝试使用下一个加载器");
            runGenerators();
        }
    }

    /**
     * 下一个状态
     *
     * @param current
     * @return
     */
    private Stage getNextStage(Stage current) {
        switch (current) {
            case INITIALIZE:
                return Stage.DATA_CACHE;
            case DATA_CACHE:
                return Stage.SOURCE;
            case SOURCE:
            case FINISHED:
                return Stage.FINISHED;
            default:
                throw new IllegalArgumentException("Unrecognized stage: " + current);
        }
    }

    interface Callback {

        void onResourceSuccess(Resource resource);

        void onLoadFailed(Throwable e);
    }

    /**
     * 当前阶段
     */
    private enum Stage {
        INITIALIZE, //初始化阶段
        DATA_CACHE, //查找文件缓存阶段
        SOURCE,
        FINISHED,
    }
}
