package com.armandorvila.poc.price.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.armandorvila.poc.price.domain.Batch;
import com.armandorvila.poc.price.domain.BatchState;

@RunWith(SpringRunner.class)
@DataMongoTest
public class BatchRepositoryTests {

	@Autowired
	private BatchRepository batchRepository;

	@Before
	public void setUp() {
		this.batchRepository.deleteAll();
	}

	@Test
	public void shouldHaveIdAndAuditFields_AfterCreation() {
		LocalDateTime time = LocalDateTime.now();
		
		Batch batch = new Batch();
		batch.setDataFile("some_file");
		batch.setState(BatchState.IN_PROGRESS);

		batch = batchRepository.save(batch);
		
		assertThat(batch).isNotNull();
		assertThat(batch.getId()).isNotEmpty();
		assertThat(batch.getCreatedAt()).isAfter(time);
		assertThat(batch.getUpdatedAt()).isAfter(time);
	}
}
