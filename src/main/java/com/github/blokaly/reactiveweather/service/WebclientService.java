package com.github.blokaly.reactiveweather.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class WebclientService {
  private final WebClient weatherStackWebClient;
  private final WebClient openWeatherMapWebClient;

  public WebclientService(
      @Qualifier("weatherStackWebClient") WebClient weatherStackWebClient,
      @Qualifier("openWeatherMapWebClient") WebClient openWeatherMapWebClient) {
    this.weatherStackWebClient = weatherStackWebClient;
    this.openWeatherMapWebClient = openWeatherMapWebClient;
  }
}
