package com.armandorvila.poc.price.config;

import java.nio.file.Paths;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.armandorvila.poc.price.domain.Price;
import com.armandorvila.poc.price.listener.BatchJobExecutionListener;
import com.armandorvila.poc.price.mapper.PriceMapper;
import com.armandorvila.poc.price.processor.PriceItemProcessor;
import com.armandorvila.poc.price.repository.BatchRepository;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

	private static final String DATA_FILE_KEY = "dataFile";

	private static final String[] FIELDS = new String[] { "instrumentId", "payload", "asOf" };

	private static final String JOB_NAME = "loadPrices";

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ApplicationConfig config;

	@Bean
	public BatchJobExecutionListener jobExecutionListener(BatchRepository repository) {
		return new BatchJobExecutionListener(repository);
	}

	@Bean
	public JobParametersValidator jobParametersValidator() {
		DefaultJobParametersValidator validator = new DefaultJobParametersValidator();
		validator.setRequiredKeys(new String[] { DATA_FILE_KEY });
		return validator;
	}

	@Bean
	public Job loadPrices(Step loadRecordsStep, BatchJobExecutionListener listener) {
		return jobBuilderFactory.get(JOB_NAME)
				.incrementer(new RunIdIncrementer())
				.validator(jobParametersValidator())
				.start(loadRecordsStep)
				.listener(listener)
				.build();
	}

	@Bean
	public Step loadRecordsStep(FlatFileItemReader<Price> reader, PriceItemProcessor processor, TaskExecutor taskExecutor) {
		return stepBuilderFactory.get(JOB_NAME).<Price, Price>chunk(config.getCommitInterval())
				.reader(reader)
				.processor(processor)
				.writer(writer())
				.taskExecutor(taskExecutor)
				.build();
	}
	
	@Bean
	@StepScope
	public PriceItemProcessor processor() {
		return new PriceItemProcessor();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Price> reader(@Value("#{jobParameters[dataFile]}") String dataFile) {
		FlatFileItemReader<Price> reader = new FlatFileItemReader<>();

		Resource resource = new FileSystemResource(Paths.get(config.getDataDirectory(), dataFile));

		reader.setResource(resource);
		reader.setLinesToSkip(1);
		reader.setLineMapper(lineMapper());

		return reader;
	}
	
	@Bean
	public MongoItemWriter<Price> writer() {
		MongoItemWriter<Price> writer = new MongoItemWriter<Price>();
		writer.setTemplate(mongoTemplate);
		writer.setCollection(config.getMongodb().getCollection());
		return writer;
	}


	private DefaultLineMapper<Price> lineMapper() {
		DefaultLineMapper<Price> lineMapper = new DefaultLineMapper<Price>();

		lineMapper.setFieldSetMapper(new PriceMapper());
		lineMapper.setLineTokenizer(lineTokenizer());

		return lineMapper;
	}

	private DelimitedLineTokenizer lineTokenizer() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(FIELDS);
		return tokenizer;
	}
}
