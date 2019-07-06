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
	
	private static final String COMPLETED_BATCH_ID = "5d20964fa24e579aa85d50c2";

	private static final String IN_PROGRESS_BATCH_ID = "5d209793a24e579aa86cb62b";

	private static final String INSTRUMENT_ID = "0006638a-8cdf-4486-b51e-1c305e30719f";

	private Price incompleteBatchPrice;
	
	private Price oldPrice;
	
	private Price newPrice;

	@Autowired
	private PriceRepository priceRepository;


	@Before
	public void setUp() {
		incompleteBatchPrice = new Price("5ab8086f1f11cd453ce85c23", "20.00", INSTRUMENT_ID, IN_PROGRESS_BATCH_ID, LocalDateTime.now().plusDays(1));
		oldPrice = new Price("5d209650a24e579aa85d50c4", "20.00", INSTRUMENT_ID, COMPLETED_BATCH_ID, LocalDateTime.now().minusDays(1));
		newPrice = new Price("5d209793a24e579aa86cb62d", "21.00", INSTRUMENT_ID, COMPLETED_BATCH_ID, LocalDateTime.now());

		Flux<Price> prices = this.priceRepository.deleteAll().thenMany(priceRepository.saveAll(asList(oldPrice, newPrice, incompleteBatchPrice)));

		StepVerifier.create(prices).expectNextCount(3).verifyComplete();
	}

	@Test
	public void should_GetFirstPage_When_GivenOffsetZero() {
		StepVerifier.create(
				priceRepository.findByInstrumentIdAndBatchIdNotIn(INSTRUMENT_ID, Flux.just(IN_PROGRESS_BATCH_ID), PageRequest.of(0, 1)))
				.expectNext(newPrice).verifyComplete();
	}

	@Test
	public void should_GetMostRecentPrice_When_FindTopByOrderByAsOf() {
		StepVerifier.create(priceRepository.findTopByInstrumentIdAndBatchIdNotInOrderByAsOfDesc(INSTRUMENT_ID, Flux.just(IN_PROGRESS_BATCH_ID)))
				.expectNext(newPrice).verifyComplete();
	}
}
