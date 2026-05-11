package com.srinjoy.weatherboy;

import java.util.List;

public class WeatherResponse {

    public String name;

    public Main main;

    public Wind wind;

    public List<Weather> weather;

    public static class Main {
        public double temp;
        public int humidity;
        public int pressure;
    }

    public static class Wind {
        public double speed;
    }

    public static class Weather {
        public String description;
        public String icon;
    }
}