package com.karashok.demookhttp;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public interface Callback {

    void onFailure(Call call, Throwable throwable);

    void onResponse(Call call, Response response);
}
