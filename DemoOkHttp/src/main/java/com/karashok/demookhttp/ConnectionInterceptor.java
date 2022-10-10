package com.karashok.demookhttp;

import java.io.IOException;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class ConnectionInterceptor implements Interceptor {

    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Request request = chain.call.request();
        HttpClient httpClient = chain.call.httpClient();
        HttpUrl httpUrl = request.httpUrl();
        HttpConnection connection = httpClient.connectionPool().get(httpUrl.getHost(), httpUrl.getPort());
        if (connection == null) {
            connection = new HttpConnection();
        }
        connection.setRequest(request);
        Response response = chain.proceed(connection);
        if (response.isKeepAlive()) {
            httpClient.connectionPool().put(connection);
        }
        return response;
    }
}
