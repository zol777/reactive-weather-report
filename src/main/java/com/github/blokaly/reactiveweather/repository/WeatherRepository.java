package com.github.blokaly.reactiveweather.repository;

import com.github.blokaly.reactiveweather.data.WeatherDao;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends ReactiveSortingRepository<WeatherDao, Long> {

}