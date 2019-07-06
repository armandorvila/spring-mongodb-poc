package com.armandorvila.poc.price.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationConfig {
	
	private String dataDirectory;
	private Mongo mongo = new Mongo();

	@Data
	public static class Mongo {
		private String collection;
	}
}
