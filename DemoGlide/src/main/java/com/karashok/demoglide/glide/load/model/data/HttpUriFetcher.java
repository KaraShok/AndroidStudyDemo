package com.karashok.demoglide.glide.load.model.data;

import android.net.Uri;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class HttpUriFetcher implements DataFetcher<InputStream> {

    private final Uri uri;
    private boolean isCanceled;

    public HttpUriFetcher(Uri uri) {
        this.uri = uri;
    }

    @Override
    public void loadData(DataFetcherCallBack<? super InputStream> callBack) {
        HttpURLConnection conn = null;
        InputStream is = null;

        try{
            URL url = new URL(uri.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            is = conn.getInputStream();
            int responseCode = conn.getResponseCode();
            if (isCanceled) {
                return;
            }
            if (responseCode == HttpURLConnection.HTTP_OK) {
                callBack.onFetcherSuccess(is);
            } else {
                callBack.onLoadFailed(new RuntimeException(conn.getResponseMessage()));
            }
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
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    public void cancel() {
        isCanceled = true;
    }

    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }
}
