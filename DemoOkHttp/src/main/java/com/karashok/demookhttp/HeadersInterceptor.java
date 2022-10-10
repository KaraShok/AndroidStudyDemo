package com.karashok.demookhttp;

import java.io.IOException;
import java.util.Map;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class HeadersInterceptor implements Interceptor {

    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Request request = chain.call.request();
        Map<String, String> headers = request.headers();
        headers.put(HttpCodec.HEAD_HOST,request.httpUrl().getHost());
        headers.put(HttpCodec.HEAD_CONNECTION,HttpCodec.HEAD_VALUE_KEEP_ALIVE);
        if (request.requestBody() != null) {
            String contentType = request.requestBody().contentType();
            if (contentType != null) {
                headers.put(HttpCodec.HEAD_CONTENT_TYPE,contentType);
            }
            long contentLength = request.requestBody().contentLength();
            if (contentLength != -1) {
                headers.put(HttpCodec.HEAD_CONTENT_LENGTH,Long.toString(contentLength));
            }
        }
        return chain.proceed();
    }
}
