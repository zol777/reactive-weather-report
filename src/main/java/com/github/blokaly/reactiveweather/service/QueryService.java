package com.github.blokaly.reactiveweather.service;

import com.github.blokaly.reactiveweather.data.OpenWeatherReport;
import com.github.blokaly.reactiveweather.data.Weather;
import com.github.blokaly.reactiveweather.data.WeatherStackReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class QueryService {
  private final WebClient weatherStackWebClient;
  private final WebClient openWeatherMapWebClient;
  private final String weatherStackKey;
  private final String openWeatherKey;

  public QueryService(
      @Qualifier("weatherStackWebClient") WebClient weatherStackWebClient,
      @Qualifier("openWeatherMapWebClient") WebClient openWeatherMapWebClient,
      @Value("${weather-stack-access-key}") String weatherStackKey,
      @Value("${open-weather-map-access-key}") String openWeatherKey) {
    this.weatherStackWebClient = weatherStackWebClient;
    this.openWeatherMapWebClient = openWeatherMapWebClient;
    this.weatherStackKey = weatherStackKey;
    this.openWeatherKey = openWeatherKey;
  }

  public Mono<Weather> lookupWeather(String city) {
    return retrieveFromWeatherStack(city).switchIfEmpty(retrieveFromOpenWeather(city));
  }

  private Mono<Weather> retrieveFromWeatherStack(String city) {
    return weatherStackWebClient.get().uri(ub -> ub.path("/current")
            .queryParam("access_key", weatherStackKey)
            .queryParam("unit", "m")
            .queryParam("query", city).build())
        .exchangeToMono(res -> res.bodyToMono(WeatherStackReport.class))
        .doOnNext(report -> log.info("Weather Stack: {}", report))
        .filter(WeatherStackReport::isSuccess)
        .map(report -> new Weather(report.getCurrent().getTemperature(), report.getCurrent()
            .getWindSpeed()));
  }

  private Mono<Weather> retrieveFromOpenWeather(String city) {
//    return Mono.just(new Weather(10.0, 10.0));
    return openWeatherMapWebClient.get().uri(ub -> ub.path("/weather")
        .queryParam("appid", openWeatherKey)
        .queryParam("units", "metric")
        .queryParam("q", city).build())
        .exchangeToMono(res -> res.bodyToMono(OpenWeatherReport.class))
        .doOnNext(report -> log.info("Open Weather: {}", report))
        .filter(OpenWeatherReport::isSuccess)
        .map(report -> new Weather(report.getMain().getTemp(), report.getWind().getSpeed()));
  }
}
