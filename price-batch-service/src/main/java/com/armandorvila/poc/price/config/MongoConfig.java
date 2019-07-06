package com.armandorvila.poc.price.config;

import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories("com.armandorvila.poc.price.repository")
@EnableMongoAuditing
public class MongoConfig {

}
