package com.karashok.demorxjava;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * 发射器
 */
public interface Emitter<T> {

    /**
     * 发射正常信号
     * @param value
     */
    void onNext(T value);

    /**
     * 发射异常信号
     * @param throwable
     */
    void onError(Throwable throwable);

    /**
     * 发射完成信号
     */
    void onComplete();
}
