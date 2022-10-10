package com.karashok.demoglide.glide.load.codec;

import android.graphics.Bitmap;

import java.io.IOException;

/**
 * @author KaraShokZ.
 * @des Bitmap 解码器
 * @since 07-16-2022
 */
public interface ResourceDecoder<T> {

    boolean handles(T source) throws IOException;

    Bitmap decode(T source, int width, int height) throws IOException;
}
