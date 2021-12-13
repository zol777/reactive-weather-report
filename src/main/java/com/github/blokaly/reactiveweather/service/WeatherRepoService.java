package com.github.blokaly.reactiveweather.service;

import com.github.blokaly.reactiveweather.data.WeatherDao;
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

import java.util.Map;

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

    public Mono<WeatherDao> retrieveWeather(String city) {
        GetItemRequest itemReq = GetItemRequest.builder().key(Map.of("city", AttributeValue.builder().s(city).build()))
                .tableName(weatherTable)
                .build();
        return Mono.fromFuture(dynamoDbAsyncClient.getItem(itemReq))
                .map(res -> new WeatherDao(city,
                        res.item().get("condition").s()))
                .doOnError(ex -> LOGGER.error("item retrieval error", ex));
    }

    public Mono<PutItemResponse> saveWeather(WeatherDao weatherDao) {
        PutItemRequest putReq = PutItemRequest.builder().item(
                        Map.of("city", AttributeValue.builder().s(weatherDao.getCity()).build(),
                                "condition", AttributeValue.builder().s(weatherDao.getCondition()).build()
                        ))
                .tableName(weatherTable)
                .build();
        return Mono.fromFuture(dynamoDbAsyncClient.putItem(putReq))
                .doOnError(ex -> LOGGER.error("item put error",ex));
    }
}
