package com.karashok.demoglide.glide.load.generator;

import android.util.Log;

import com.karashok.demoglide.glide.GlideContext;
import com.karashok.demoglide.glide.cache.DiskCache;
import com.karashok.demoglide.glide.cache.Key;
import com.karashok.demoglide.glide.load.model.ModelLoader;
import com.karashok.demoglide.glide.load.model.data.DataFetcher;

import java.io.File;
import java.util.List;

/**
 * @author KaraShokZ.
 * @des 专门负责文件缓存查找的生成器
 * @since 07-17-2022
 */
public class DataCacheGenerator implements DataGenerator, DataFetcher.DataFetcherCallBack<Object> {

    private static final String TAG = "DataCacheGenerator";

    private final DataGeneratorCallBack cb;
    private final GlideContext glideContext;
    private final DiskCache diskCache;

    private List<ModelLoader<File,?>> modelLoaders;
    private List<Key> keys;
    private int sourceIdIndex = -1;
    private Key sourceKey;
    private int modelLoaderIndex;
    private File cacheFile;
    private ModelLoader.LoadData<?> loadData;

    public DataCacheGenerator(GlideContext glideContext,
                              DiskCache diskCache,
                              Object model,
                              DataGeneratorCallBack dataGeneratorCallBack) {
        this.glideContext = glideContext;
        this.cb = dataGeneratorCallBack;
        this.diskCache = diskCache;

        // 获得对应类型的所有key (当前只有ObjectKey用于磁盘缓存，磁盘缓存不需要宽、高等)
        // 即如果文件缓存 获得缓存的key
        keys = glideContext.getRegistry().getKeys(model);
    }

    @Override
    public boolean startNext() {
        // modelLoaders为null 继续查找
        Log.d(TAG, "startNext: 磁盘加载器开始加载");

        // 负责将 注册的model 转换为需要打 data
        // 我们注册了 将http地址/文件地址 转化为InputStream
        while (modelLoaders == null) {
            sourceIdIndex++;
            if (sourceIdIndex >= keys.size()) {
                return false;
            }

            // 通过model获得的一个Key 缓存【key:value】
            Key sourceId = keys.get(sourceIdIndex);

            // 获得磁盘缓存的文件
            cacheFile = diskCache.get(sourceId);
            Log.d(TAG, "startNext: 磁盘缓存存在则位于：" + cacheFile);
            if (cacheFile != null) {
                sourceKey = sourceId;

                Log.d(TAG, "startNext: 获得所有文件加载器");

                // 获得所有的文件加载器
                modelLoaders = glideContext.getRegistry().getModelLoaders(cacheFile);
                modelLoaderIndex = 0;
            }
        }

        boolean started = false;

        // 找出好几个File为Model的 Loader 直到确定一个完全满足
        // 即 能够由此Loader解析完成
        while (!started && hasNextModelLoader()) {
            ModelLoader<File, ?> modelLoader = modelLoaders.get(modelLoaderIndex++);
            loadData = modelLoader.buildData(cacheFile);

            Log.d(TAG, "startNext: 获得加载设置数据");

            // 是否可以把此loader加载的Data 解码出Bitmap
            if (loadData != null && glideContext.getRegistry().hasLoadPath(loadData.fetcher.getDataClass())) {
                Log.d(TAG, "startNext: 加载设置数据输出数据对应能够查找有效的解码器路径,开始加载数据");
                started = true;
                loadData.fetcher.loadData(this);
            }
        }
        return started;
    }

    private boolean hasNextModelLoader() {
        return modelLoaderIndex < modelLoaders.size();
    }

    @Override
    public void cancel() {
        if (loadData != null) {
            loadData.fetcher.cancel();
        }
    }

    @Override
    public void onFetcherSuccess(Object o) {
        Log.d(TAG, "onFetcherSuccess: 加载器加载数据成功回调");
        cb.onDataReady(sourceKey,o, DataGeneratorCallBack.DataSource.CACHE);
    }

    @Override
    public void onLoadFailed(Exception e) {
        Log.d(TAG, "onLoadFailed: 加载器加载数据失败回调");
        cb.onDataFetcherFailed(sourceKey,e);
    }
}
