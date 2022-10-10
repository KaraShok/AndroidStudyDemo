package com.karashok.demookhttp;

import java.io.IOException;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public interface Interceptor {

    Response intercept(InterceptorChain chain) throws IOException;
}
