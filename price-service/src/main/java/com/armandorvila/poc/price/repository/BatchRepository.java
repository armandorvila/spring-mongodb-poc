package com.armandorvila.poc.price.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.armandorvila.poc.price.domain.Batch;
import com.armandorvila.poc.price.domain.BatchState;

import reactor.core.publisher.Flux;

@Repository
public interface BatchRepository extends ReactiveMongoRepository<Batch, String> {

    @Query("{ id: { $exists: true }}")
    Flux<Batch> findAll(final Pageable page);
    
    Flux<Batch> findByState(BatchState state);
}
