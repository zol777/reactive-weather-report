package com.github.blokaly.reactiveweather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class UnexpectedWeatherVersionException extends NotFoundException {

  public UnexpectedWeatherVersionException(Long expectedVersion, Long foundVersion) {
    super(String.format(
        "The weather has a different version than the expected one. Expected [%s], found [%s]",
        expectedVersion, foundVersion));
  }

}
