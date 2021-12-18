package com.github.blokaly.reactiveweather.data;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "lastModifiedDate", ignore = true)
  @Mapping(target = "temperature", source = "temperatureDegrees")
  WeatherDao toWeatherDao(Weather weather);

  @Mapping(target = "temperatureDegrees", source = "temperature")
  Weather toWeather(WeatherDao dao);
}
