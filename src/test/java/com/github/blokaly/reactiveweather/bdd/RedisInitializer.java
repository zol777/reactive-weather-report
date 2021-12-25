package com.github.blokaly.reactiveweather.bdd;

import com.github.blokaly.reactiveweather.container.TestRedisContainer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class RedisInitializer  implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if ( "docker".equalsIgnoreCase(applicationContext.getEnvironment().getProperty("spring.profiles.active"))) {
            return;
        }
        TestRedisContainer.getInstance().start();
    }
}