package com.weatherforecast.repository;

import com.weatherforecast.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherData, Long> {
    List<WeatherData> findTop365ByOrderByDateAsc();
}
