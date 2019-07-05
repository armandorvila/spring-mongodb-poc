package com.armandorvila.poc.price.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.armandorvila.poc.price.domain.Price;
import com.armandorvila.poc.price.listener.BadRecordSkipListener;
import com.armandorvila.poc.price.listener.LoadPricesJobExecutionListener;
import com.armandorvila.poc.price.mapper.PriceMapper;
import com.armandorvila.poc.price.repository.BatchRunRepository;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

	private static final String MONGO_COLLECTION = "prices";

	private static final String BASE_DATA_PATH = "/Users/armandorvila/work/ihs-assignment/data/";

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Bean
	public Job loadPrices(Step loadRecordsStep, LoadPricesJobExecutionListener listener) {
		return jobBuilderFactory.get("loadPrices").incrementer(new RunIdIncrementer()).start(loadRecordsStep)
				.listener(listener).build();
	}

	@Bean
	public Step loadRecordsStep(FlatFileItemReader<Price> reader) {
		return stepBuilderFactory.get("loadRecords").<Price, Price>chunk(10000).reader(reader).faultTolerant()
				.skipLimit(100).skip(FlatFileParseException.class).listener(new BadRecordSkipListener())
				.writer(writer()).build();
	}

	@Bean
	public MongoItemWriter<Price> writer() {
		MongoItemWriter<Price> writer = new MongoItemWriter<Price>();
		writer.setTemplate(mongoTemplate);
		writer.setCollection(MONGO_COLLECTION);
		return writer;
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Price> reader(@Value("#{jobParameters[dataFile]}") String dataFile) {
		FlatFileItemReader<Price> reader = new FlatFileItemReader<>();

		reader.setLinesToSkip(1);
		reader.setResource(fileResource(dataFile));
		reader.setLineMapper(lineMapper());

		return reader;
	}

	private DefaultLineMapper<Price> lineMapper() {
		return new DefaultLineMapper<Price>() {
			{
				setLineTokenizer(lineTokenizer());
				setFieldSetMapper(new PriceMapper());
			}
		};
	}

	private DelimitedLineTokenizer lineTokenizer() {
		return new DelimitedLineTokenizer() {
			{
				setNames(new String[] { "id", "payload", "asOf" });
				setStrict(false);
			}
		};
	}

	private Resource fileResource(String dataFile) {
		final Path path = Paths.get(BASE_DATA_PATH, dataFile);
		return new FileSystemResource(path);
	}

	@Bean
	public LoadPricesJobExecutionListener loadPricesJobExecutionListener(BatchRunRepository repository) {
		return new LoadPricesJobExecutionListener(repository);
	}
}
