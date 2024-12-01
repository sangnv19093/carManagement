package com.sangnv3.carmanagement.Api;


import com.sangnv3.carmanagement.Model.Car;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @GET("cars")
    Call<List<Car>> getAllCars();

    @GET("cars/{id}")
    Call<Car> getCarById(@Path("id") String id);

    @POST("cars")
    Call<Car> addCar(@Body Car car);

    @PUT("cars/{id}")
    Call<Car> updateCar(@Path("id") String id, @Body Car car);

    @DELETE("cars/{id}")
    Call<Void> deleteCar(@Path("id") String id);
}
