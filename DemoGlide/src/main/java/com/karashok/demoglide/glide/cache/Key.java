package com.karashok.demoglide.glide.cache;

import java.security.MessageDigest;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-03-2022
 */
public interface Key {

    void updateDiskCacheKey(MessageDigest md);

    byte[] getKeyBytes();
}
