package com.armandorvila.poc.price.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationConfig {
	
	private String dataDirectory;
	
	private Integer commitInterval;
	
	private MongoDB mongodb = new MongoDB();

	@Data
	public static class MongoDB {
		private String collection;
	}
}
