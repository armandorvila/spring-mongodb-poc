package com.armandorvila.poc.price.resource;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.armandorvila.poc.price.domain.Price;
import com.armandorvila.poc.price.repository.PriceRepository;
import com.armandorvila.poc.price.resource.PriceResource;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(PriceResource.class)
@AutoConfigureWebTestClient
public class PriceResourceTests {

	private Price price = new Price("5ab8086f1f11cd453ce85c23", "21", "0006638a-8cdf-4486-b51e-1c305e30719f", LocalDateTime.now());
	
	@MockBean
	private PriceRepository priceRespository;
	
	@Autowired
	private WebTestClient webClient;
	
	@Test
	public void should_GetLastPrice_When_Called_LastPrice() {
		given(priceRespository.findTopByInstrumentIdOrderByAsOfDesc(price.getInstrumentId()))
		  .willReturn(Mono.just(price));
		
		webClient.get().uri("/prices/last")
					.accept(APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectHeader().contentType(APPLICATION_JSON_UTF8)
					.expectBody(Price.class)
					.isEqualTo(price);
		
	 then(priceRespository).should(times(1)).findTopByInstrumentIdOrderByAsOfDesc(price.getInstrumentId());
	}
}