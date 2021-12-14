package com.github.blokaly.reactiveweather.controller;

import com.github.blokaly.reactiveweather.data.Weather;
import com.github.blokaly.reactiveweather.service.CacheService;
import com.github.blokaly.reactiveweather.service.QueryService;
import com.github.blokaly.reactiveweather.service.RepoService;
import com.github.blokaly.reactiveweather.service.ValidationService;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
@Slf4j
public class WeatherController {
  private final ValidationService validationService;
  private final CacheService cacheService;
  private final RepoService repoService;
  private final QueryService queryService;

  public WeatherController(
      ValidationService validationService,
      CacheService cacheService,
      RepoService repoService,
      QueryService queryService) {
    this.validationService = validationService;
    this.cacheService = cacheService;
    this.repoService = repoService;
    this.queryService = queryService;
  }

  @GetMapping("/weather/{city}")
  public Mono<Weather> lookupWeather(@PathVariable String city) {
    return validationService.validate(city)
        .flatMap(cacheService::retrieveWeather)
        .switchIfEmpty(
            queryService.lookupWeather(city)
                .transform(it -> cacheService.saveWeather(city, it))
                .transform(it -> repoService.saveWeather(city, it))
        )
        .switchIfEmpty(repoService.retrieveWeather(city));
  }

}
