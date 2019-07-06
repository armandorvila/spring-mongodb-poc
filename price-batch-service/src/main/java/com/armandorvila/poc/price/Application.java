package com.armandorvila.poc.price;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.armandorvila.poc.price.config.ApplicationConfig;

@SpringBootApplication
@EnableMongoAuditing
@EnableConfigurationProperties(ApplicationConfig.class)
@EnableMongoRepositories("com.armandorvila.poc.price.repository")
public class Application {
	  public static void main(String[] args) {
		    SpringApplication.run(Application.class, args);
		  }
}
