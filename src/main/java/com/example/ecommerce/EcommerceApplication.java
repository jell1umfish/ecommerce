package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(EcommerceApplication.class, args);
		Environment env = context.getBean(Environment.class);
		String dbUrl = env.getProperty("spring.datasource.url");

		System.out.println("=======================================");
		System.out.println("DEBUG: Database URL from config: " + dbUrl);
		System.out.println("=======================================");
	}
}