package com.armandorvila.poc.price.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.armandorvila.poc.price.domain.Batch;

@Repository
public interface BatchRepository extends MongoRepository<Batch, String> {

}
