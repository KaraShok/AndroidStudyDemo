package com.karashok.demorxjava;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * 观察者
 */
public interface Observe<T> {

    /**
     * 已经订阅
     * @param d
     */
    void onSubscribe(Disposable d);

    /**
     * 正常数据回调
     * @param t
     */
    void onNext(T t);

    /**
     * 异常数据回调
     * @param e
     */
    void onError(Throwable e);

    /**
     * 成功回调
     */
    void onComplete();
}
