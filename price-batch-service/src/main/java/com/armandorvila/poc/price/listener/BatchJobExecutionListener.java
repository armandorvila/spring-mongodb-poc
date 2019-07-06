package com.armandorvila.poc.price.listener;

import java.util.Optional;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import com.armandorvila.poc.price.domain.Batch;
import com.armandorvila.poc.price.domain.BatchState;
import com.armandorvila.poc.price.repository.BatchRepository;

public class BatchJobExecutionListener implements JobExecutionListener {

	private BatchRepository batchRepository;

	public BatchJobExecutionListener(BatchRepository batchRepository) {
		this.batchRepository = batchRepository;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		String dataFile = jobExecution.getJobParameters().getString("dataFile");

		Batch batch = new Batch();

		batch.setDataFile(dataFile);
		batch.setState(BatchState.IN_PROGRESS);

		batch = batchRepository.save(batch);

		jobExecution.getExecutionContext().putString(Batch.class.getSimpleName(), batch.getId());

	}

	@Override
	public void afterJob(JobExecution jobExecution) {

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			String batchId = jobExecution.getExecutionContext().getString(Batch.class.getSimpleName());

			Optional<Batch> batch = batchRepository.findById(batchId);

			if (batch.isPresent()) {
				batch.get().setState(BatchState.COMPLETED);
				batchRepository.save(batch.get());
			}
		}

	}

}
