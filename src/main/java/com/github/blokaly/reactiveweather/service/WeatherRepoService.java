package com.github.blokaly.reactiveweather.service;

import com.github.blokaly.reactiveweather.data.Weather;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

@Repository
public class WeatherRepoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherRepoService.class);
    private final DynamoDbAsyncClient dynamoDbAsyncClient;
    private final String weatherTable;

    public WeatherRepoService(DynamoDbAsyncClient dynamoDbAsyncClient,
                              @Value("${application.dynamo.table}") String weatherTable) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
        this.weatherTable = weatherTable;
    }

    public Mono<Weather> retrieveWeather(String city) {
        GetItemRequest itemReq = GetItemRequest.builder().key(Map.of("city", AttributeValue.builder().s(city).build()))
                .tableName(weatherTable)
                .build();
        return Mono.fromFuture(dynamoDbAsyncClient.getItem(itemReq))
                .map(res -> new Weather(Double.parseDouble(res.item().get("temperature").s()), Double.parseDouble(res.item().get("windSpeed").s())))
                .doOnError(ex -> LOGGER.error("item retrieval error", ex));
    }

    public Mono<PutItemResponse> saveWeather(String city, Weather weather) {
        PutItemRequest putReq = PutItemRequest.builder().item(
                        Map.of("city", AttributeValue.builder().s(city).build(),
                                "temperature", AttributeValue.builder().s(String.valueOf(weather.getTemperature())).build(),
                                "windSpeed", AttributeValue.builder().s(String.valueOf(weather.getWindSpeed())).build()
                        ))
                .tableName(weatherTable)
                .build();
        return Mono.fromFuture(dynamoDbAsyncClient.putItem(putReq))
                .doOnError(ex -> LOGGER.error("item put error",ex));
    }
}
