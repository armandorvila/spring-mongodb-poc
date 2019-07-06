package com.armandorvila.poc.price.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

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
import com.armandorvila.poc.price.repository.BatchRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BatchResourceTests {
	
	private static final String COMPLETED_BATCH_ID = "5d20964fa24e579aa85d50c2";

	private static final String IN_PROGRESS_BATCH_ID = "5d209793a24e579aa86cb62b";
	
	@Autowired
	protected WebTestClient webClient;

	@Autowired
	private BatchRepository batchRepository;
	
	@Before
	public void setUp() {
		Flux<Batch> batches = this.batchRepository.deleteAll()
				.thenMany(batchRepository.saveAll(Arrays.asList(
				new Batch(IN_PROGRESS_BATCH_ID, "sample-data.csv", BatchState.IN_PROGRESS), 
				new Batch(COMPLETED_BATCH_ID, "sample-data.csv", BatchState.COMPLETED))));
		
		StepVerifier.create(batches).expectNextCount(2).verifyComplete();
	}
	
	@Test
	public void should_BatchesPage_When_Call_GetBatches() {
		
		webClient.get().uri("/api/batches")
					.accept(APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectHeader().contentType(APPLICATION_JSON_UTF8)
					.expectBodyList(Batch.class)
					.hasSize(2);
	}


}

