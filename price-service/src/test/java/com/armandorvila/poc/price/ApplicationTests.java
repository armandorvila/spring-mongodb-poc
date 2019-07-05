package com.armandorvila.poc.price;

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

import com.armandorvila.poc.price.domain.Price;
import com.armandorvila.poc.price.repository.PriceRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {
	
	private final Price oldPrice = new Price("5ab8086f1f11cd453ce85c23", "20.00", "0006638a-8cdf-4486-b51e-1c305e30719f", LocalDateTime.now().minusDays(1));
	private final Price newPrice = new Price("5ab8086f1f11cd453ce85c23", "21.00", "0006638a-8cdf-4486-b51e-1c305e30719f", LocalDateTime.now());
	
	@Autowired
	protected WebTestClient webClient;
	
	@Autowired
	private PriceRepository priceRepository;

	
	@Before
	public void setUp() {
		Flux<Price> prices = this.priceRepository.deleteAll()
				.thenMany(priceRepository.saveAll(Arrays.asList(
						oldPrice,
						newPrice)));
		
		StepVerifier.create(prices).expectNextCount(2).verifyComplete();
	}
	
	@Test
	public void should_GetLastPrice_When_Called_LastPrice() {
		
		webClient.get().uri("/prices/last?instrumentId={instrumentId}", oldPrice.getInstrumentId())
					.accept(APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectHeader().contentType(APPLICATION_JSON_UTF8)
					.expectBody(Price.class)
					.isEqualTo(newPrice);
	}


}

