package com.karashok.demoglide.glide.load.model.data;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.InputStream;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class FileUriFetcher implements DataFetcher<InputStream>{

    private final Uri uri;
    private final ContentResolver cr;

    public FileUriFetcher(Uri uri, ContentResolver cr) {
        this.uri = uri;
        this.cr = cr;
    }

    @Override
    public void loadData(DataFetcherCallBack<? super InputStream> callBack) {
        InputStream is = null;
        try{
            is = cr.openInputStream(uri);
            callBack.onFetcherSuccess(is);
        } catch (Exception e) {
            callBack.onLoadFailed(e);
        } finally{
            if (is != null) {
                try{
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void cancel() {

    }

    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }
}
