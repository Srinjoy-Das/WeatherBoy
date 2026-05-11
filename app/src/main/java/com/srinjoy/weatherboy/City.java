package com.srinjoy.weatherboy;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cities")
public class City {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "city_name")
    public String cityName;

    public City(String cityName) {
        this.cityName = cityName;
    }
}
