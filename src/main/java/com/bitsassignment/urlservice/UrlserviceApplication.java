package com.bitsassignment.urlservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UrlserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlserviceApplication.class, args);
	}

}
