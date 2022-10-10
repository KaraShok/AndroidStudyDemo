package com.karashok.demoglide.glide.cache;

import java.io.File;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-03-2022
 */
public interface DiskCache {

    interface Writer {
        boolean write(File file);
    }

    File get(Key key);

    void put(Key key, Writer writer);

    void delete(Key key);

    void clear();
}
