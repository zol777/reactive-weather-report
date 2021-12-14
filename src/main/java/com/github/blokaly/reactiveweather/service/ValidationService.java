package com.github.blokaly.reactiveweather.service;

import com.github.blokaly.reactiveweather.exception.WeatherException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ValidationService {
  private final Pattern cityNamePattern = Pattern.compile("[^a-zA-Z ]");

  public Mono<String> validate(String city) {
    if ( cityNamePattern.matcher(URLDecoder.decode(city, StandardCharsets.UTF_8)).find()) {
      throw new WeatherException(HttpStatus.BAD_REQUEST, "city name validation failed");
    }
    return Mono.just(city);
  }
}
