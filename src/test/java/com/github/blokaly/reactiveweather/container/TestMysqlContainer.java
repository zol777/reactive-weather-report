package com.github.blokaly.reactiveweather.container;

import org.testcontainers.containers.MySQLContainer;

public class TestMysqlContainer extends MySQLContainer<TestMysqlContainer> {
    private static final String IMAGE_VERSION = "mysql:latest";
    private static final int EXPOSED_PORT = 3306;
    private static TestMysqlContainer container;

    private TestMysqlContainer() {
        super(IMAGE_VERSION);
    }

    public static TestMysqlContainer getInstance(String dbName) {
        if (container == null) {
            container = new TestMysqlContainer()
                    .withDatabaseName(dbName)
                    .withExposedPorts(EXPOSED_PORT);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_HOST", container.getHost());
        System.setProperty("DB_PORT", String.valueOf(container.getMappedPort(EXPOSED_PORT)));
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }
}
