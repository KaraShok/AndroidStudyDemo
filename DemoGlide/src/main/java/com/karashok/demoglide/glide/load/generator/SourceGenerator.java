package com.karashok.demoglide.glide.load.generator;

import android.util.Log;

import com.karashok.demoglide.glide.GlideContext;
import com.karashok.demoglide.glide.load.model.ModelLoader;
import com.karashok.demoglide.glide.load.model.data.DataFetcher;

import java.util.List;

/**
 * @author KaraShokZ.
 * @des 专门负责从图片源地址加载数据的生成器
 * @since 07-17-2022
 */
public class SourceGenerator implements DataGenerator, DataFetcher.DataFetcherCallBack<Object> {

    private static final String TAG = "SourceGenerator";

    private final DataGeneratorCallBack cb;
    private GlideContext glideContext;
    private int loadDataListIndex;
    private List<ModelLoader.LoadData<?>> loadDatas;
    private ModelLoader.LoadData<?> loadData;

    public SourceGenerator(DataGeneratorCallBack cb,Object model, GlideContext glideContext) {
        this.cb = cb;
        this.glideContext = glideContext;
        loadDatas = glideContext.getRegistry().getLoadDatas(model);
    }

    @Override
    public boolean startNext() {
        Log.d(TAG, "startNext: 源加载器开始加载");
        boolean started = false;
        while (!started && hasNextModelLoader()) {
            loadData = loadDatas.get(loadDataListIndex++);
            Log.d(TAG, "startNext: 获得加载设置数据");

            // hasLoadPath : 是否有个完整的加载路径 从将Model转换为Data之后 有没有一个对应的将Data
            // 转换为图片的解码器
            if (loadData != null && glideContext.getRegistry().hasLoadPath(loadData.fetcher.getDataClass())) {
                Log.d(TAG, "startNext: 加载设置数据输出数据对应能够查找有效的解码器路径,开始加载数据");
                started = true;

                // 将Model转换为Data
                loadData.fetcher.loadData(this);

            }
        }
        return started;
    }

    /**
     * 是否有下一个modelloader支持加载
     * @return
     */
    private boolean hasNextModelLoader() {
        return loadDataListIndex < loadDatas.size();
    }

    @Override
    public void cancel() {
        if (loadData != null) {
            loadData.fetcher.cancel();
        }
    }

    /**
     * 成功由Model获得一个Data
     * @param data
     */
    @Override
    public void onFetcherSuccess(Object o) {
        Log.d(TAG, "onFetcherSuccess: 加载器加载数据成功回调");
        cb.onDataReady(loadData.key,o, DataGeneratorCallBack.DataSource.REMOTE);
    }

    @Override
    public void onLoadFailed(Exception e) {
        Log.d(TAG, "onLoadFailed: 加载器加载数据失败回调");
        cb.onDataFetcherFailed(loadData.key, e);
    }
}
