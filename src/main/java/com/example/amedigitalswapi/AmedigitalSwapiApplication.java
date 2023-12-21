package com.example.amedigitalswapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AmedigitalSwapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmedigitalSwapiApplication.class, args);
	}

}
