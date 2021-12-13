package com.github.blokaly.reactiveweather.controller;

import com.github.blokaly.reactiveweather.data.Weather;
import com.github.blokaly.reactiveweather.service.WeatherRepoService;
import com.github.blokaly.reactiveweather.service.WebclientService;
import java.time.Duration;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
@Slf4j
public class WeatherController {
  private final ReactiveRedisOperations<String, Weather> weatherOps;
  private final WeatherRepoService weatherRepoService;
  private final WebclientService webclientService;
  @Value("${application.cache.time-to-live}")
  private int ttl;

  public WeatherController(ReactiveRedisOperations<String, Weather> weatherOps,
                           WeatherRepoService weatherRepoService,
                           WebclientService webclientService) {
    this.weatherOps = weatherOps;
    this.weatherRepoService = weatherRepoService;
    this.webclientService = webclientService;
  }

  @GetMapping("/weather/{city}")
  public Mono<Weather> lookupWeather(@PathVariable String city) {
    return validateCity(city)
        .flatMap(it -> weatherOps.opsForValue().get(it))
        .doOnNext(it -> log.info("Value from cache: {}", it))
        .switchIfEmpty(
            webclientService.lookupWeather(city)
                .zipWhen(it -> weatherOps.opsForValue().set(city, it, Duration.ofSeconds(ttl)))
                .doOnNext(it -> log.info("Saved to cache: {}", it.getT2()))
                .map(Tuple2::getT1)
                .zipWhen(it -> weatherRepoService.saveWeather(city, it))
                .map(Tuple2::getT1))
        .switchIfEmpty(weatherRepoService.retrieveWeather(city));
  }

  private Mono<String> validateCity(String city) {
    return Mono.just(city);
  }
}
