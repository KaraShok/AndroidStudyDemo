package com.karashok.demoglide.glide.load.model;

import android.net.Uri;

import java.io.File;
import java.io.InputStream;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class StringModelLoader implements ModelLoader<String, InputStream> {

    private final ModelLoader<Uri, InputStream> loader;

    public StringModelLoader(ModelLoader<Uri, InputStream> loader) {
        this.loader = loader;
    }

    @Override
    public boolean handles(String s) {
        return true;
    }

    @Override
    public LoadData<InputStream> buildData(String s) {
        Uri uri;
        if (s.startsWith("/")) {
            uri = Uri.fromFile(new File(s));
        } else {
            uri = Uri.parse(s);
        }
        return loader.buildData(uri);
    }

    public static class StreamFactory implements ModelLoaderaFactory<String, InputStream> {

        @Override
        public ModelLoader<String, InputStream> build(ModelLoaderRegistry registry) {
            return new StringModelLoader(registry.build(Uri.class, InputStream.class));
        }
    }
}
