package com.srinjoy.weatherboy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class WeatherViewModel extends ViewModel {

    private final WeatherRepository repository = new WeatherRepository();
    public LiveData<WeatherResponse> getWeatherLiveData() {
        return repository.weatherLiveData;
    }
    public void fetchWeather(String city, String apiKey) {
        repository.fetchWeather(city, apiKey);
    }
}
