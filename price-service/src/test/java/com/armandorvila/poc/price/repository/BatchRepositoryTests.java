package com.armandorvila.poc.price.repository;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.armandorvila.poc.price.domain.Batch;
import com.armandorvila.poc.price.domain.BatchState;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
public class BatchRepositoryTests {

	private static final Batch IN_PROGRESS = new Batch("5d20964fa24e579aa85d50c2", "some_file", BatchState.IN_PROGRESS);
	private static final Batch COMPLETED = new Batch("5d209793a24e579aa86cb62b", "some_file", BatchState.COMPLETED);

	@Autowired
	private BatchRepository batchRepository;

	private Flux<Batch> batches;

	@Before
	public void setUp() {
		this.batches = this.batchRepository.deleteAll().thenMany(batchRepository.saveAll(asList(IN_PROGRESS, COMPLETED)));
		StepVerifier.create(batches).expectNextCount(2).verifyComplete();
	}

	@Test
	public void should_GetFirstPage_When_PageIsZero() {
		StepVerifier.create(
				batchRepository.findAll(PageRequest.of(0, 1))).assertNext(batch -> assertThat(batch.getId()).isEqualTo(IN_PROGRESS.getId()))
				.verifyComplete();
	}

	@Test
	public void should_GetUncompletedBatches_When_FindBatchIdsByState() {
		StepVerifier.create(batchRepository.findByState(BatchState.IN_PROGRESS).map(b -> b.getId()))
				.expectNext(IN_PROGRESS.getId()).verifyComplete();
	}
	
	@Test
	public void should_GetCompletedBatches_When_FindBatchIdsByState() {
		StepVerifier.create(batchRepository.findByState(BatchState.COMPLETED).map(b -> b.getId()))
				.expectNext(COMPLETED.getId()).verifyComplete();
	}
}
