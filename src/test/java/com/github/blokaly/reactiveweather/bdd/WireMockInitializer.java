package com.github.blokaly.reactiveweather.bdd;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

@Slf4j
public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {

    WireMockServer wireMockServer =
        new WireMockServer(new WireMockConfiguration().dynamicPort());

    wireMockServer.start();
    log.info("WireMock server started");

    applicationContext.addApplicationListener(applicationEvent -> {
      if (applicationEvent instanceof ContextClosedEvent) {
        wireMockServer.stop();
        log.info("WireMock server stopped");
      }
    });

    applicationContext.getBeanFactory()
        .registerSingleton("wireMockServer", wireMockServer);

    TestPropertyValues
        .of(Map.of("application.weather-stack.api", wireMockServer.baseUrl() + "/weatherstack/",
            "application.open-weather-map.api", wireMockServer.baseUrl() + "/openweather/"))
        .applyTo(applicationContext);
  }
}
