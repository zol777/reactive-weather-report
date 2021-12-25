package com.github.blokaly.reactiveweather.bdd;

import com.github.blokaly.reactiveweather.container.TestMysqlContainer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class MysqlInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        String dbName = applicationContext.getEnvironment().getProperty("database.name");
        TestMysqlContainer.getInstance(dbName).start();
    }
}
