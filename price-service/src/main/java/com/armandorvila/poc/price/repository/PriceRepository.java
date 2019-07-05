package com.armandorvila.poc.price.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.armandorvila.poc.price.domain.Price;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PriceRepository extends ReactiveMongoRepository<Price, String> {

    Flux<Price> findByInstrumentId(String instrumentId, Pageable page);

	Mono<Price> findTopByInstrumentIdOrderByAsOfDesc(String instrumentId);
}
