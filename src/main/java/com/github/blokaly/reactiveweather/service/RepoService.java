package com.github.blokaly.reactiveweather.service;

import com.github.blokaly.reactiveweather.data.Weather;
import com.github.blokaly.reactiveweather.data.WeatherDao;
import com.github.blokaly.reactiveweather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepoService {
  private final WeatherRepository weatherRepository;

  @Transactional
  public Mono<Weather> saveWeather(Mono<Weather> upstream) {
    return upstream.zipWhen(it -> {
          var dao = new WeatherDao().setCity(it.getCity()).setTemperature(it.getTemperatureDegrees())
              .setWindSpeed(it.getWindSpeed());
          return weatherRepository.save(dao);
        })
        .doOnError(ex -> log.error("Error saving to datatabse", ex))
        .map(Tuple2::getT1);
  }

  public Mono<Void> clearWeather() {
    return weatherRepository.deleteAll();
  }
}
