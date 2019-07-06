package com.armandorvila.poc.price.processor;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;

import com.armandorvila.poc.price.domain.Batch;
import com.armandorvila.poc.price.domain.Price;

public class PriceItemProcessorTests {

	private static final String BATCH_ID = "1234";

	private PriceItemProcessor itemProcessor = new PriceItemProcessor();

	private JobExecution jobExecution;

	@Before
	public void setUp() {
		jobExecution = MetaDataInstanceFactory.createJobExecution();
		jobExecution.getExecutionContext().putString(Batch.class.getName(), BATCH_ID);
		itemProcessor.beforeStep(MetaDataInstanceFactory.createStepExecution(jobExecution, "loadPrices", 1L));
	}

	@Test
	public void shouldSetBatchId() throws Exception {
		Price price = new Price("priceId", "25.00", "instrumentId", BATCH_ID, LocalDateTime.now());
		
		itemProcessor.process(price);
		
		assertThat(price.getBatchId()).isEqualTo(BATCH_ID);
	}
}
