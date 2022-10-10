package com.karashok.demoglide.glide.load.model;

import com.karashok.demoglide.glide.cache.Key;
import com.karashok.demoglide.glide.load.model.data.DataFetcher;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public interface ModelLoader<Model, Data> {

    /**
     * 此Loader是否能够处理对应Model的数据
     *
     * @param model
     * @return
     */
    boolean handles(Model model);

    /**
     * 创建加载数据
     *
     * @param model
     * @return
     */
    LoadData<Data> buildData(Model model);

    interface ModelLoaderaFactory<Model, Data> {

        ModelLoader<Model, Data> build(ModelLoaderRegistry registry);
    }

    class LoadData<Data> {

        // 缓存的key
        public final Key key;

        // 加载数据
        public final DataFetcher<Data> fetcher;

        public LoadData(Key key, DataFetcher<Data> fetcher) {
            this.key = key;
            this.fetcher = fetcher;
        }
    }
}
