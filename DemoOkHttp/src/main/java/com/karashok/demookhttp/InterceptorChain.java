package com.karashok.demookhttp;

import java.io.IOException;
import java.util.List;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class InterceptorChain {

    private final List<Interceptor> interceptors;

    private final int index;

    public final Call call;

    public HttpConnection httpConnection;

    final HttpCodec httpCodec = new HttpCodec();

    public InterceptorChain(List<Interceptor> interceptors, int index, Call call,
                            HttpConnection httpConnection) {
        this.interceptors = interceptors;
        this.index = index;
        this.call = call;
        this.httpConnection = httpConnection;
    }

    public Response proceed() throws IOException {
        return proceed(httpConnection);
    }

    public Response proceed(HttpConnection httpConnection) throws IOException {
        Interceptor interceptor = interceptors.get(index);
        InterceptorChain next = new InterceptorChain(interceptors,index + 1,call,httpConnection);
        Response response = interceptor.intercept(next);
        return response;
    }
}
