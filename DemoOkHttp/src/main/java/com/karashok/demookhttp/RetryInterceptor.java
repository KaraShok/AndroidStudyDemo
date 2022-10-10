package com.karashok.demookhttp;

import java.io.IOException;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class RetryInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        for (int i = 0; i < chain.call.httpClient().retrys(); i++) {
            if (chain.call.isCanceled()) {
                throw new IOException("Canceled");
            }
            try{
                return chain.proceed();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
        return chain.proceed();
    }
}
