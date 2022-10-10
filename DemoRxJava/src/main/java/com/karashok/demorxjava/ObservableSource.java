package com.karashok.demorxjava;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * 被观察者订阅函数
 */
public interface ObservableSource<T> {

    void subscribe(Observe<? super T> observer);
}

