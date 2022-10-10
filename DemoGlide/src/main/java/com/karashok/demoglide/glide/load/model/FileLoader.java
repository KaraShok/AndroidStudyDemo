package com.karashok.demoglide.glide.load.model;

import android.net.Uri;

import java.io.File;
import java.io.InputStream;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class FileLoader<Data> implements ModelLoader<File, Data> {

    private final ModelLoader<Uri, Data> loader;

    public FileLoader(ModelLoader<Uri, Data> loader) {
        this.loader = loader;
    }

    @Override
    public boolean handles(File file) {
        return true;
    }

    @Override
    public LoadData<Data> buildData(File file) {
        return loader.buildData(Uri.fromFile(file));
    }

    public static class Factory implements ModelLoaderaFactory<File, InputStream> {

        @Override
        public ModelLoader<File, InputStream> build(ModelLoaderRegistry registry) {
            return new FileLoader<>(registry.build(Uri.class,InputStream.class));
        }
    }
}
