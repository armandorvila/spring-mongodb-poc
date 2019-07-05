package com.armandorvila.poc.price.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.armandorvila.poc.price.domain.BatchRun;

@Repository
public interface BatchRunRepository extends MongoRepository<BatchRun, String> {

}
