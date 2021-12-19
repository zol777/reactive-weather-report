package com.github.blokaly.reactiveweather.controller;

import com.github.blokaly.reactiveweather.data.Weather;
import com.github.blokaly.reactiveweather.service.CacheService;
import com.github.blokaly.reactiveweather.service.QueryService;
import com.github.blokaly.reactiveweather.service.RepoService;
import com.github.blokaly.reactiveweather.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WeatherController {
  private final ValidationService validationService;
  private final CacheService cacheService;
  private final QueryService queryService;
  private final RepoService repoService;

  @GetMapping("/weather/{city}")
  public Mono<Weather> lookupWeather(@PathVariable String city) {
    return validationService.validate(city)
        .flatMap(cacheService::retrieveWeather)
        .switchIfEmpty(
            queryService.lookupWeather(city)
                .transform(cacheService::saveWeather)
                .transform(repoService::upsert))
        .switchIfEmpty(repoService.retrieveWeather(city));
  }

}
