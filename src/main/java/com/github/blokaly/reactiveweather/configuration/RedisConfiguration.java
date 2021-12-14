package com.github.blokaly.reactiveweather.configuration;

import com.github.blokaly.reactiveweather.data.Weather;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
  @Bean
  ReactiveRedisOperations<String, Weather> redisOperations(ReactiveRedisConnectionFactory factory) {
    RedisSerializationContext.RedisSerializationContextBuilder<String, Weather> builder =
        RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
    var context = builder.value(new Jackson2JsonRedisSerializer<>(Weather.class)).build();
    return new ReactiveRedisTemplate<>(factory, context);
  }

}