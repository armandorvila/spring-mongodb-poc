package com.armandorvila.poc.price.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.armandorvila.poc.price.domain.Batch;
import com.armandorvila.poc.price.domain.BatchState;
import com.armandorvila.poc.price.domain.Price;
import com.armandorvila.poc.price.repository.BatchRepository;
import com.armandorvila.poc.price.repository.PriceRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PriceResourceTests {
	
	private static final String COMPLETED_BATCH_ID = "5d20964fa24e579aa85d50c2";

	private static final String IN_PROGRESS_BATCH_ID = "5d209793a24e579aa86cb62b";

	private static final String INSTRUMENT_ID = "0006638a-8cdf-4486-b51e-1c305e30719f";

	private Price incompleteBatchPrice;
	
	private Price oldPrice;
	
	private Price newPrice;
	
	@Autowired
	protected WebTestClient webClient;
	
	@Autowired
	private PriceRepository priceRepository;

	@Autowired
	private BatchRepository batchRepository;
	
	@Before
	public void setUp() {
		incompleteBatchPrice = new Price("5ab8086f1f11cd453ce85c23", "20.00", INSTRUMENT_ID, IN_PROGRESS_BATCH_ID, LocalDateTime.now().plusDays(1));
		oldPrice = new Price("5d209650a24e579aa85d50c4", "20.00", INSTRUMENT_ID, COMPLETED_BATCH_ID, LocalDateTime.now().minusDays(1));
		newPrice = new Price("5d209793a24e579aa86cb62d", "21.00", INSTRUMENT_ID, COMPLETED_BATCH_ID, LocalDateTime.now());
		
		Flux<Price> prices = this.priceRepository.deleteAll()
				.thenMany(priceRepository.saveAll(Arrays.asList(
						incompleteBatchPrice,
						oldPrice,
						newPrice)));
		
		StepVerifier.create(prices).expectNextCount(3).verifyComplete();
		
		Flux<Batch> batches = this.batchRepository.deleteAll()
				.thenMany(batchRepository.saveAll(Arrays.asList(
				new Batch(IN_PROGRESS_BATCH_ID, "sample-data.csv", BatchState.IN_PROGRESS), 
				new Batch(COMPLETED_BATCH_ID, "sample-data.csv", BatchState.COMPLETED))));
		
		StepVerifier.create(batches).expectNextCount(2).verifyComplete();
	}
	
	@Test
	public void should_GetLastValidPrice_When_Called_LastPrice() {
		
		webClient.get().uri("/api/prices/last?instrumentId={instrumentId}", INSTRUMENT_ID)
					.accept(APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectHeader().contentType(APPLICATION_JSON_UTF8)
					.expectBody(Price.class)
					.isEqualTo(newPrice);
	}
	
	@Test
	public void should_GetOneElementPage_When_OffsetZero_And_LimitOne() {
		
		webClient.get().uri("/api/prices?instrumentId={instrumentId}&offset={offset}&limit={limit}", INSTRUMENT_ID, 0, 1)
					.accept(APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectHeader().contentType(APPLICATION_JSON_UTF8)
					.expectBodyList(Price.class)
					.hasSize(1);
	}
	
	@Test
	public void should_GetTwoElementsPage_When_OffsetZero_And_LimitTwo() {
		
		webClient.get().uri("/api/prices?instrumentId={instrumentId}&offset={offset}&limit={limit}", INSTRUMENT_ID, 0, 2)
					.accept(APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectHeader().contentType(APPLICATION_JSON_UTF8)
					.expectBodyList(Price.class)
					.hasSize(2);
	}
	
	@Test
	public void should_OneElementPage_When_OffsetOne_And_LimitOne() {
		
		webClient.get().uri("/api/prices?instrumentId={instrumentId}&offset={offset}&limit={limit}", INSTRUMENT_ID, 1, 1)
					.accept(APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectHeader().contentType(APPLICATION_JSON_UTF8)
					.expectBodyList(Price.class)
					.hasSize(1);
	}
}

