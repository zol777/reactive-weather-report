package com.github.blokaly.reactiveweather.exception;


public class WeatherNotFoundException extends NotFoundException {

  public WeatherNotFoundException(Long id) {
    super(String.format("Weather with id [%d] is not found", id));
  }

}
