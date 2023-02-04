package com.erebelo.springh2demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringH2DemoApplication {

	public static void main(String[] args) {

		System.out.println("Initializing...");
		SpringApplication.run(SpringH2DemoApplication.class, args);
	}

}
