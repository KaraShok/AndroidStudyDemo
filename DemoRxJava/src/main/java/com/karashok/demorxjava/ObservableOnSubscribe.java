package com.karashok.demorxjava;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * 被观察者创建数据
 */
public interface ObservableOnSubscribe<T> {

    /**
     * 订阅函数
     * @param emitter
     * @throws Exception
     */
    void subscribe(Emitter<T> emitter) throws Exception;
}
