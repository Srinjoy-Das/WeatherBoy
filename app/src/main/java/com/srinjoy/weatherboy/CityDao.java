package com.srinjoy.weatherboy;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CityDao {

    @Insert
    void insert(City city);

    @Query("SELECT * FROM cities ORDER BY id DESC")
    List<City> getAllCities();

    @Delete
    void delete(City city);
}