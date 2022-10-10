package com.karashok.demoglide.glide.load.model;

import android.net.Uri;

import com.karashok.demoglide.glide.load.ObjectKey;
import com.karashok.demoglide.glide.load.model.data.HttpUriFetcher;

import java.io.InputStream;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class HttpUriLoader implements ModelLoader<Uri, InputStream> {

    /**
     * http类型的uri此loader才支持
     *
     * @param uri
     * @return
     */
    @Override
    public boolean handles(Uri uri) {
        String scheme = uri.getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }

    @Override
    public LoadData<InputStream> buildData(Uri uri) {
        return new LoadData<InputStream>(new ObjectKey(uri), new HttpUriFetcher(uri));
    }

    public static class Factory implements ModelLoaderaFactory<Uri, InputStream> {


        @Override
        public ModelLoader<Uri, InputStream> build(ModelLoaderRegistry registry) {
            return new HttpUriLoader();
        }
    }
}
