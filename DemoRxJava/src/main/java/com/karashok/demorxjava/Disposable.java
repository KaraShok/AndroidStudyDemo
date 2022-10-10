package com.karashok.demorxjava;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * 判断订阅是否有效
 */
public interface Disposable {

    /**
     * 判断订阅是否取消
     * @return
     */
    Boolean isDisposable();

    /**
     * 取消订阅
     */
    void disposable();
}
