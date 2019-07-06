package com.armandorvila.poc.price;

import static org.assertj.core.api.Assertions.assertThat;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.armandorvila.poc.price.domain.Batch;
import com.armandorvila.poc.price.domain.BatchState;
import com.armandorvila.poc.price.listener.BatchJobExecutionListener;
import com.armandorvila.poc.price.processor.PriceItemProcessor;
import com.armandorvila.poc.price.repository.BatchRepository;
import com.mongodb.client.MongoCollection;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	private static final String JOB_NAME = "loadPrices";

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private JobOperator jobOperator;

	@Autowired
	private JobExplorer jobExplorer;

	@Autowired
	private BatchRepository batchRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void setUp() {
		mongoTemplate.getCollection("prices").drop();
		mongoTemplate.getCollection("batches").drop();
	}

	@Test
	public void shouldHave_SpringBatchBeans_When_ProperlyConfigured() {
		assertThat(ctx.getBean(Job.class)).isNotNull();
		assertThat(ctx.getBean(Step.class)).isNotNull();
		assertThat(ctx.getBean(PriceItemProcessor.class)).isNotNull();
		assertThat(ctx.getBean(FlatFileItemReader.class)).isNotNull();
		assertThat(ctx.getBean(MongoItemWriter.class)).isNotNull();
		assertThat(ctx.getBean(MongoTemplate.class)).isNotNull();
		assertThat(ctx.getBean(BatchJobExecutionListener.class)).isNotNull();
		assertThat(ctx.getBean(JobParametersValidator.class)).isNotNull();
	}

	@Test
	public void shouldUpdateBatchRun_When_ExecutionCompleted() throws Exception {
		Long executionId = jobOperator.start(JOB_NAME, "dataFile=sample-data-small.csv");

		while (jobExplorer.getJobExecution(executionId).getStatus().isRunning()) {
			Thread.sleep(1000);
		}

		assertThat(jobExplorer.getJobExecution(executionId).getStatus()).isEqualTo(BatchStatus.COMPLETED);

		Batch batch = batchRepository.findAll().get(0);
		
		assertThat(batch.getState()).isEqualTo(BatchState.COMPLETED);
		assertThat(batch.getDataFile()).isEqualTo("sample-data-small.csv");
		
		MongoCollection<Document> prices = mongoTemplate.getCollection("prices");
		
		assertThat(prices.countDocuments()).isEqualTo(20434);
		assertThat(prices.find().first().getString("batchId")).isEqualTo(batch.getId());
	}

	@Test
	public void shouldFail_And_NotUpdateBatchRun_When_FileDoesntExist() throws Exception {
		Long executionId = jobOperator.start(JOB_NAME, "dataFile=non-existent-file.csv");

		while (jobExplorer.getJobExecution(executionId).getStatus().isRunning()) {
			Thread.sleep(1000);
		}

		assertThat(jobExplorer.getJobExecution(executionId).getStatus()).isEqualTo(BatchStatus.FAILED);

		assertThat(batchRepository.findAll().get(0).getState()).isEqualTo(BatchState.IN_PROGRESS);
		assertThat(mongoTemplate.getCollection("prices").countDocuments()).isEqualTo(0);
	}

	@Test
	public void shouldFail_And_NotUpdateBatchRun_When_DataIsWrong() throws Exception {
		Long executionId = jobOperator.start(JOB_NAME, "dataFile=sample-data-wrong.csv");

		while (jobExplorer.getJobExecution(executionId).getStatus().isRunning()) {
			Thread.sleep(1000);
		}

		assertThat(jobExplorer.getJobExecution(executionId).getStatus()).isEqualTo(BatchStatus.FAILED);

		assertThat(batchRepository.findAll().get(0).getState()).isEqualTo(BatchState.IN_PROGRESS);
		assertThat(mongoTemplate.getCollection("prices").countDocuments()).isEqualTo(0);
	}

	@Test(expected = JobParametersInvalidException.class)
	public void shouldFail_And_NotUpdateBatchRun_When_ParametersAreWrong() throws Exception {
		jobOperator.start(JOB_NAME, "dataF=wrongKey");
	}

}