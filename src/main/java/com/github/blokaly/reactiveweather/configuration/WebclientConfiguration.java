package com.github.blokaly.reactiveweather.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebclientConfiguration {

  @Bean("weatherStackWebClient")
  public WebClient weatherStackWebClient(
      @Value("${application.weather-stack.api}") String weatherStackApi,
      WebClient.Builder webClientBuilder) {

    return webClientBuilder
        .baseUrl(weatherStackApi)
        .build();
  }

  @Bean("openWeatherMapWebClient")
  public WebClient openWeatherMapWebClient(
      @Value("${application.open-weather-map.api}") String openWeatherMapApi,
      WebClient.Builder webClientBuilder) {

    return webClientBuilder
        .baseUrl(openWeatherMapApi)
        .build();
  }
}
