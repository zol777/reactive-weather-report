package com.github.blokaly.reactiveweather.configuration;

import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.lang.NonNull;

import java.time.Duration;

@Configuration
public class R2dbcConfiguration extends AbstractR2dbcConfiguration {

  @Value("${database.name}")
  private String database;

  @Value("${database.host}")
  private String host;

  @Value("${database.port:5432}")
  private int port;

  @Value("${database.username}")
  private String username;

  @Value("${database.password}")
  private String password;

  @Override
  @Bean
  @NonNull
  public ConnectionFactory connectionFactory() {

    ConnectionFactory connectionFactory =
       MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
            .host(host)
            .port(port)
            .username(username)
            .password(password)
            .database(database)
            .build());

    ConnectionPoolConfiguration configuration =
        ConnectionPoolConfiguration.builder(connectionFactory)
            .maxIdleTime(Duration.ofMinutes(30))
            .initialSize(5)
            .maxSize(10)
            .maxCreateConnectionTime(Duration.ofSeconds(3))
            .build();

    return new ConnectionPool(configuration);
  }

}