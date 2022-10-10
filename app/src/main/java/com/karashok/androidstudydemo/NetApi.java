package com.karashok.androidstudydemo;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public interface NetApi {

    @GET("/api/columns/{user} ")
    Call<Object> getAuthor(@Path("user") String user);

    @POST("/api/columns/{user} ")
    Observable<Object> getAuthor1(@Path("user") String user);
}
