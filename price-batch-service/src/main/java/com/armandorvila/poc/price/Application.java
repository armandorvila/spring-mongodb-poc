package com.armandorvila.poc.price;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.armandorvila.poc.price.config.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class Application {
	  public static void main(String[] args) {
		    SpringApplication.run(Application.class, args);
		  }
}
