package com.karashok.demoglide.glide.cache;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-03-2022
 */
public interface ArrayPool {

    byte[] get(int len);

    void put(byte[] data);


    void clearMemory();

    void trimMemory(int level);

    int getMaxSize();
}
