package com.karashok.demoglide.glide.load.generator;

import com.karashok.demoglide.glide.cache.Key;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public interface DataGenerator {

    boolean startNext();

    void cancel();

    interface DataGeneratorCallBack {

        enum DataSource {
            REMOTE,
            CACHE
        }

        void onDataReady(Key sourceKey, Object data, DataSource dataSource);

        void onDataFetcherFailed(Key sourceKey, Exception e);
    }
}
