package com.finedustlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FinedustlabApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinedustlabApplication.class, args);
	}

}
