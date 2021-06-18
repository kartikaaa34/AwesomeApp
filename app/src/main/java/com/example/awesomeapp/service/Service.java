package com.example.awesomeapp.service;

import com.example.awesomeapp.model.Content;
import com.example.awesomeapp.util.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Service {

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            //.addInterceptor(offlineInterceptor)
            //.addNetworkInterceptor(onlineInterceptor)
            .cache(null)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("curated")
    Call<Content> loadContent(@Header("Authorization") String auth,
                              @Query("page") int page,
                              @Query("per_page") int perPage);

}
