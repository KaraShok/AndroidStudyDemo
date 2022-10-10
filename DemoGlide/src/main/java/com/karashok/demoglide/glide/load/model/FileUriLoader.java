package com.karashok.demoglide.glide.load.model;

import android.content.ContentResolver;
import android.net.Uri;

import com.karashok.demoglide.glide.load.ObjectKey;
import com.karashok.demoglide.glide.load.model.data.FileUriFetcher;

import java.io.InputStream;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class FileUriLoader implements ModelLoader<Uri, InputStream> {

    private final ContentResolver contentResolver;

    public FileUriLoader(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public boolean handles(Uri uri) {
        return ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme());
    }

    @Override
    public LoadData<InputStream> buildData(Uri uri) {
        return new LoadData<>(new ObjectKey(uri),new FileUriFetcher(uri,contentResolver));
    }

    public static class Factory implements ModelLoaderaFactory<Uri, InputStream> {

        private final ContentResolver contentResolver;

        public Factory(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }

        @Override
        public ModelLoader<Uri, InputStream> build(ModelLoaderRegistry registry) {
            return new FileUriLoader(contentResolver);
        }
    }
}
