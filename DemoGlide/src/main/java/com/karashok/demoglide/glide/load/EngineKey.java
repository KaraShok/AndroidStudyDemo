package com.karashok.demoglide.glide.load;

import com.karashok.demoglide.glide.cache.Key;

import java.security.MessageDigest;
import java.util.Objects;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class EngineKey implements Key {

    private final Object model;
    private final int width;
    private final int height;

    public EngineKey(Object model, int width, int height) {
        this.model = model;
        this.width = width;
        this.height = height;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest md) {
        md.update(getKeyBytes());
    }

    @Override
    public byte[] getKeyBytes() {
        return toString().getBytes();
    }

    @Override
    public String toString() {
        return "EngineKey{" +
                "model=" + model +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
