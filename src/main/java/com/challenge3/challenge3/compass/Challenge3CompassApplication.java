package com.challenge3.challenge3.compass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Challenge3CompassApplication {

	public static void main(String[] args) {
		SpringApplication.run(Challenge3CompassApplication.class, args);
	}

}
