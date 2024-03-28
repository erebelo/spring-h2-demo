package com.erebelo.springh2demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringH2DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringH2DemoApplication.class, args);
	}
}
