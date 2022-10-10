package com.karashok.demoglide.glide.load;

import com.karashok.demoglide.glide.cache.Key;

import java.security.MessageDigest;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class ObjectKey implements Key {

    private final Object object;

    public ObjectKey(Object object) {
        this.object = object;
    }

    /**
     * 当磁盘缓存的时候 key只能是字符串
     * ObjectKey变成一个字符串
     * 序列化:json
     * <p>
     * 将ObjectKey转变成一个字符串的手段
     *
     * @param md md5/sha1
     */
    @Override
    public void updateDiskCacheKey(MessageDigest md) {
        md.update(getKeyBytes());
    }

    @Override
    public byte[] getKeyBytes() {
        return object.toString().getBytes();
    }
}
