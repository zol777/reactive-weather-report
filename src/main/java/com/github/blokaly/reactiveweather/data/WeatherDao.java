package com.github.blokaly.reactiveweather.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDao {
    private String city;
    private double temperature;
    private double windSpeed;
}