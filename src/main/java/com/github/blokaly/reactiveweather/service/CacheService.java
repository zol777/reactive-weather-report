package com.github.blokaly.reactiveweather.service;

import com.github.blokaly.reactiveweather.data.Weather;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@Slf4j
public class CacheService {
  private final ReactiveRedisOperations<String, Weather> weatherOps;
  private final int ttl;

  public CacheService(
      ReactiveRedisOperations<String, Weather> weatherOps,
      @Value("${application.cache.time-to-live}") int ttl) {
    this.weatherOps = weatherOps;
    this.ttl = ttl;
  }

  public Mono<Weather> retrieveWeather(String city) {
    return weatherOps.opsForValue().get(city)
        .doOnNext(it -> log.info("Value from cache: {}", it));
  }

  public Mono<Boolean> saveWeather(String city, Weather weather) {
    return weatherOps.opsForValue().set(city, weather, Duration.ofSeconds(ttl))
        .doOnNext(it -> log.info("Saved to cache: {}", it));
  }

  public Mono<Weather> saveWeather(String city, Mono<Weather> upstream) {
    return upstream.zipWhen(it -> saveWeather(city, it)).map(Tuple2::getT1);
  }
}
