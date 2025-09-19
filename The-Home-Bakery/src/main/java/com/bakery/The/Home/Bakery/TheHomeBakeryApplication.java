package com.bakery.The.Home.Bakery;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@PropertySource(value="classpath:config/application.secrets.properties",ignoreResourceNotFound = true)
@EnableScheduling
public class TheHomeBakeryApplication {

	public static void main(String[] args) {
		// Load .env file BEFORE Spring starts
		Dotenv dotenv = Dotenv.configure()
				.directory("./") // Look in root folder
				.ignoreIfMissing() // Don't crash if .env file is missing
				.load();

		// Set system properties from .env
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);

		// Now start Spring application
		SpringApplication.run(TheHomeBakeryApplication.class, args);
	}
}