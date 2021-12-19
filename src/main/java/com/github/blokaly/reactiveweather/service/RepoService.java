package com.github.blokaly.reactiveweather.service;

import com.github.blokaly.reactiveweather.data.Weather;
import com.github.blokaly.reactiveweather.data.WeatherMapper;
import com.github.blokaly.reactiveweather.exception.UnexpectedWeatherVersionException;
import com.github.blokaly.reactiveweather.exception.WeatherNotFoundException;
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
  private final WeatherMapper weatherMapper;

  @Transactional
  public Mono<Weather> saveWeather(Mono<Weather> upstream) {
    return upstream.zipWhen(it -> weatherRepository.save(weatherMapper.toWeatherDao(it)))
        .doOnError(ex -> log.error("Error saving to datatabse", ex))
        .map(Tuple2::getT1);
  }

  public Mono<Weather> retrieveWeather(String city) {
    return weatherRepository.findWeatherDaoByCity(city).map(weatherMapper::toWeather);
  }

  public Mono<Weather> retrieveWeather(final Long id, final Long version) {
    return weatherRepository.findById(id)
        .switchIfEmpty(Mono.error(new WeatherNotFoundException(id)))
        .handle((dao, sink) -> {
          // Optimistic locking: pre-check
          if (version != null && !version.equals(dao.getVersion())) {
            // The version are different, return an error
            sink.error(new UnexpectedWeatherVersionException(version, dao.getVersion()));
          } else {
            sink.next(weatherMapper.toWeather(dao));
          }
        });
  }

  public Mono<Void> clearWeather() {
    return weatherRepository.deleteAll();
  }
}
