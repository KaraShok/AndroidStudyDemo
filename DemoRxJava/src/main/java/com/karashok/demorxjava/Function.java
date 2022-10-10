package com.karashok.demorxjava;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * Map 功能转换函数
 */
public interface Function<T, R> {

    /**
     * 数据转换
     * @param t
     * @return
     */
    R apply(T t);
}
