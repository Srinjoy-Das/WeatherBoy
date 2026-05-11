package com.srinjoy.weatherboy;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {
    public MutableLiveData<WeatherResponse> weatherLiveData =
            new MutableLiveData<>();
    public void fetchWeather(String city, String apiKey) {
        RetrofitClient.getApiService()
                .getWeather(city, apiKey, "metric")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call,
                                           Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null)
                        {
                            weatherLiveData.postValue(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<WeatherResponse> call,
                                          Throwable t) {
                    }
                });
    }
}
