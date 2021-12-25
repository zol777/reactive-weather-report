package com.github.blokaly.reactiveweather.bdd.stepdefs;

import com.github.blokaly.reactiveweather.bdd.MysqlInitializer;
import com.github.blokaly.reactiveweather.bdd.RedisInitializer;
import com.github.blokaly.reactiveweather.bdd.WireMockInitializer;
import com.github.blokaly.reactiveweather.service.RepoService;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = {MysqlInitializer.class, RedisInitializer.class, WireMockInitializer.class})
@CucumberContextConfiguration
@Slf4j
public abstract class CucumberSpringContextConfiguration {

  @Autowired
  protected WebTestClient webTestClient;

  @LocalServerPort
  protected Integer port;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  protected WireMockServer wireMockServer;

  @Autowired
  protected ReactiveRedisConnectionFactory redisFactory;

  @Autowired
  protected RepoService repoService;
}