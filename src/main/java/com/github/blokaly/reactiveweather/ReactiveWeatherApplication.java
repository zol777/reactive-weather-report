package com.github.blokaly.reactiveweather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ReactiveWeatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveWeatherApplication.class, args);
//		new SpringApplicationBuilder(ReactiveWeatherApplication.class)
//				.web(WebApplicationType.REACTIVE).run(args);
	}

}
