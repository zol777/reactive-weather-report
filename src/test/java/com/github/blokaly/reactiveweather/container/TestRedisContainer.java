package com.github.blokaly.reactiveweather.container;

import org.testcontainers.containers.GenericContainer;

public class TestRedisContainer extends GenericContainer<TestRedisContainer> {

    private static final String IMAGE_VERSION = "redis:alpine";
    private static final int EXPOSED_PORT = 6379;
    private static TestRedisContainer container;

    private TestRedisContainer() {
        super(IMAGE_VERSION);
    }

    public static TestRedisContainer getInstance() {
        if (container == null) {
            container = new TestRedisContainer()
                    .withExposedPorts(EXPOSED_PORT);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("CACHE_HOST", container.getHost());
        System.setProperty("CACHE_PORT", String.valueOf(container.getMappedPort(EXPOSED_PORT)));
    }
}
