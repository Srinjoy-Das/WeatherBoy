package com.srinjoy.weatherboy;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    // Search weather by city name
    @GET("weather")
    Call<WeatherResponse> getWeather(
            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units
    );

    // Search weather by coordinates (GPS)
    @GET("weather")
    Call<WeatherResponse> getWeatherByCoordinates(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}