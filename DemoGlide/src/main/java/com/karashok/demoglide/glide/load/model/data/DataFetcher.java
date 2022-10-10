package com.karashok.demoglide.glide.load.model.data;

/**
 * @author KaraShokZ.
 * @des 负责数据获取
 * @since 07-17-2022
 */
public interface DataFetcher<Data> {

    void loadData(DataFetcherCallBack<? super Data> callBack);

    void cancel();

    Class<?> getDataClass();

    interface DataFetcherCallBack<Data> {

        /**
         * 数据加载完成
         */
        void onFetcherSuccess(Data data);

        /**
         * 加载失败
         *
         * @param e
         */
        void onLoadFailed(Exception e);
    }
}
