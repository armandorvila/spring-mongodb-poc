package com.armandorvila.poc.price.listener;

import java.util.Optional;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import com.armandorvila.poc.price.domain.BatchRun;
import com.armandorvila.poc.price.domain.BatchRunState;
import com.armandorvila.poc.price.repository.BatchRunRepository;

public class LoadPricesJobExecutionListener implements JobExecutionListener {

	private BatchRunRepository batchRunRepository;

	public LoadPricesJobExecutionListener(BatchRunRepository batchRunRepository) {
		this.batchRunRepository = batchRunRepository;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		String dataFile = jobExecution.getJobParameters().getString("dataFile");

		BatchRun batchRun = new BatchRun();

		batchRun.setDataFile(dataFile);
		batchRun.setState(BatchRunState.IN_PROGRESS);

		batchRun = batchRunRepository.save(batchRun);

		jobExecution.getExecutionContext().putString(BatchRun.class.getSimpleName(), batchRun.getId());

	}

	@Override
	public void afterJob(JobExecution jobExecution) {

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			String batchRunId = jobExecution.getExecutionContext().getString(BatchRun.class.getSimpleName());

			Optional<BatchRun> batchRun = batchRunRepository.findById(batchRunId);

			if (batchRun.isPresent()) {
				batchRun.get().setState(BatchRunState.COMPLETED);
				batchRunRepository.save(batchRun.get());
			}
		}

	}

}
