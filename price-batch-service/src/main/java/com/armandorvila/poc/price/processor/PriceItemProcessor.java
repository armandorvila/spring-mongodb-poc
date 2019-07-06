package com.armandorvila.poc.price.processor;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import com.armandorvila.poc.price.domain.Batch;
import com.armandorvila.poc.price.domain.Price;

public class PriceItemProcessor implements ItemProcessor<Price, Price>{

	private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }
	
	@Override
	public Price process(Price price) throws Exception {
		String batchId = jobExecution.getExecutionContext().getString(Batch.class.getName());
		
		price.setBatchId(batchId);
		
		return price;
	}

}
