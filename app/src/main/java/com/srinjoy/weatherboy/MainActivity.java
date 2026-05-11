package com.srinjoy.weatherboy;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import java.util.List;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity {

    // Replace with your actual OpenWeather API key
    private static final String API_KEY = "6e62b338ea04cc301d1873fcdbf53f2a";

    private EditText etCity;
    private Button btnSearch;
    private ImageView ivWeatherIcon;
    private TextView tvTemperature;
    private TextView tvCity;
    private TextView tvDescription;
    private TextView tvHumidity;
    private TextView tvWind;
    private TextView tvPressure;
    private static final int LOCATION_PERMISSION_REQUEST = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocationWeather();

        initializeViews();

        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "weather-db"
                )
                .allowMainThreadQueries()
                .build();

        List<City> cities = db.cityDao().getAllCities();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocationWeather();

        btnSearch.setOnClickListener(v -> {
            String city = etCity.getText().toString().trim();

            if (TextUtils.isEmpty(city)) {
                Toast.makeText(MainActivity.this,
                        "Please enter a city name",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            fetchWeather(city);
        });

        fetchWeather("Kolkata");
    }

    private void initializeViews() {
        etCity = findViewById(R.id.etCity);
        btnSearch = findViewById(R.id.btnSearch);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvCity = findViewById(R.id.tvCity);
        tvDescription = findViewById(R.id.tvDescription);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvWind = findViewById(R.id.tvWind);
        tvPressure = findViewById(R.id.tvPressure);
    }


    private void getCurrentLocationWeather() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        fetchWeatherByCoordinates(
                                location.getLatitude(),
                                location.getLongitude());
                    }
                });
    }

    private void fetchWeatherByCoordinates(double lat, double lon) {
        WeatherApiService apiService = RetrofitClient.getApiService();
        apiService.getWeatherByCoordinates(lat, lon, API_KEY, "metric")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call,
                                           Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateUI(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<WeatherResponse> call,
                                          Throwable t) {
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocationWeather();
        }
    }

        private void fetchWeather(String city) {
        WeatherApiService apiService = RetrofitClient.getApiService();

        Call<WeatherResponse> call =
                apiService.getWeather(city, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call,
                                   Response<WeatherResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                    db.cityDao().insert(new City(city));
                } else {
                    Toast.makeText(MainActivity.this,
                            "City not found",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Network Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUI(WeatherResponse weather) {
        // Temperature
        int temp = (int) weather.main.temp;
        tvTemperature.setText(temp + "°C");

        // City Name
        tvCity.setText(weather.name);

        // Description
        String description = weather.weather.get(0).description;
        tvDescription.setText(capitalize(description));

        // Humidity
        tvHumidity.setText("Humidity: " + weather.main.humidity + "%");

        // Wind Speed
        tvWind.setText("Wind Speed: " + weather.wind.speed + " m/s");

        // Pressure
        tvPressure.setText("Pressure: " + weather.main.pressure + " hPa");

        // Weather Icon
        String iconCode = weather.weather.get(0).icon;
        String iconUrl =
                "https://openweathermap.org/img/wn/" +
                        iconCode +
                        "@2x.png";

        Glide.with(this)
                .load(iconUrl)
                .into(ivWeatherIcon);
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        String[] words = text.split(" ");
        StringBuilder builder = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                builder.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return builder.toString().trim();
    }
}