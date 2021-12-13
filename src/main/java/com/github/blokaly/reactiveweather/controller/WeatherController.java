package com.github.blokaly.reactiveweather.controller;

import com.github.blokaly.reactiveweather.data.Weather;
import com.github.blokaly.reactiveweather.data.WeatherDao;
import com.github.blokaly.reactiveweather.service.WeatherRepo;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class WeatherController {
    private final ReactiveRedisOperations<String, Weather> weatherOps;
    private final WeatherRepo weatherRepo;

    public WeatherController(ReactiveRedisOperations<String, Weather> weatherOps, WeatherRepo weatherRepo) {
        this.weatherOps = weatherOps;
        this.weatherRepo = weatherRepo;
    }

    @GetMapping("/weather/{city}")
    public Mono<Weather> lookupWeather(@PathVariable String city) {
        return validateCity(city)
                .flatMap(it -> weatherOps.opsForValue().get(it))
                .switchIfEmpty(weatherRepo.retrieveWeather(city).map(weatherDao -> new Weather(weatherDao.getCondition())));
    }

    @PostMapping("/weather")
    public Mono<Weather> saveWeather(@RequestBody Weather weather) {
        return weatherOps.opsForValue().set("beijing", weather, Duration.ofSeconds(5))
                .then(weatherRepo.saveWeather(new WeatherDao("beijing", weather.getCondition()+"-dynamo")))
                .map(putItemResponse -> weather);
    }

    private Mono<String> validateCity(String city) {
        return Mono.just(city);
    }
}
