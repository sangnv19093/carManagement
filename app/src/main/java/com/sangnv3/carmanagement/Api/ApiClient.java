package com.sangnv3.carmanagement.Api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Sử dụng địa chỉ IP phù hợp với môi trường của bạn
    // Nếu sử dụng trình giả lập, sử dụng 10.0.2.2
    private static final String BASE_URL = "http://192.168.1.6:3000/api/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Tạo interceptor để log chi tiết
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client) // Sử dụng client có interceptor
                    .build();
        }
        return retrofit;
    }
}
