package com.karashok.demookhttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class CallServiceInterceptor implements Interceptor {

    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        HttpCodec httpCodec = chain.httpCodec;
        InputStream is = chain.httpConnection.call(httpCodec);
        String readLine = httpCodec.readLine(is);
        HashMap<String, String> headers = httpCodec.readHeaders(is);

        boolean isKeepAlive = Objects.equals(headers.getOrDefault(HttpCodec.HEAD_CONNECTION,""),HttpCodec.HEAD_VALUE_KEEP_ALIVE);
        int contentLength = Integer.valueOf(headers.getOrDefault(HttpCodec.HEAD_CONTENT_LENGTH,"-1"));
        boolean isChunked = Objects.equals(headers.getOrDefault(HttpCodec.HEAD_TRANSFER_ENCODING,""),HttpCodec.HEAD_VALUE_CHUNKED);

        String body = null;
        if (contentLength > 0) {
            body = new String(httpCodec.readBytes(is,contentLength));
        } else if (isChunked) {
            body = httpCodec.readChunked(is);
        }

        String[] split = readLine.split(" ");
        chain.httpConnection.updateLastUseTime();

        return new Response(Integer.valueOf(split[1]),contentLength,headers,body,isKeepAlive);
    }
}
