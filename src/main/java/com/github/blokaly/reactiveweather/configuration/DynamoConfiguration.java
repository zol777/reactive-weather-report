package com.github.blokaly.reactiveweather.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class DynamoConfiguration {
    private final String region;
    private final String endpoint;

    public DynamoConfiguration(@Value("${application.dynamo.region}") String region,
                               @Value("${application.dynamo.endpoint}") String endpoint) {
        this.region = region;
        this.endpoint = endpoint;
    }

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient() {
        return DynamoDbAsyncClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .build();
    }
}