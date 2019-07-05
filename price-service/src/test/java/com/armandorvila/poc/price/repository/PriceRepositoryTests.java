package com.armandorvila.poc.price.repository;

import static java.util.Arrays.asList;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.armandorvila.poc.price.domain.Price;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PriceRepositoryTests {

	private final Price oldPrice = new Price("5ab8086f1f11cd453ce85c23", "20.00", "0006638a-8cdf-4486-b51e-1c305e30719f", LocalDateTime.now().minusDays(1));
	private final Price newPrice = new Price("5ab8086f1f11cd453ce85c23", "21.00", "0006638a-8cdf-4486-b51e-1c305e30719f", LocalDateTime.now());

	@Autowired
	private PriceRepository priceRepository;

	private Flux<Price> prices;

	@Before
	public void setUp() {
		this.prices = this.priceRepository.deleteAll().thenMany(priceRepository.saveAll(asList(oldPrice, newPrice)));
		StepVerifier.create(prices).expectNextCount(2).verifyComplete();
	}

	@Test
	public void should_GetFirstPage_When_GivenPageOne() {
		StepVerifier.create(priceRepository.findByInstrumentId(newPrice.getInstrumentId(), PageRequest.of(0, 1))).expectNext(newPrice)
				.expectNextCount(0).verifyComplete();
	}

	@Test
	public void should_GetOnePrice_When_GivenPageSizeOne() {
		StepVerifier.create(priceRepository.findByInstrumentId(newPrice.getInstrumentId(),PageRequest.of(0, 1))).expectNextCount(1).verifyComplete();
	}

	@Test
	public void should_GetMostRecentPrice_When_FindTopByOrderByAsOf() {
		StepVerifier.create(priceRepository.findTopByInstrumentIdOrderByAsOfDesc(newPrice.getInstrumentId()))
		.expectNext(newPrice)
		.verifyComplete();
	}
}
