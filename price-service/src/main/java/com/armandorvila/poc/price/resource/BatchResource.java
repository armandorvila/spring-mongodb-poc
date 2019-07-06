package com.armandorvila.poc.price.resource;

import static com.armandorvila.poc.price.resource.Pagination.DEFAULT_LIMIT;
import static com.armandorvila.poc.price.resource.Pagination.DEFAULT_OFFSET;
import static com.armandorvila.poc.price.resource.Pagination.toPageRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.armandorvila.poc.price.domain.Batch;
import com.armandorvila.poc.price.repository.BatchRepository;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class BatchResource {

	private BatchRepository batchRepository;

	public BatchResource(BatchRepository batchRepository) {
		this.batchRepository = batchRepository;
	}

	@GetMapping("/batches")
	public Flux<Batch> getBatches(
			@RequestParam(defaultValue = DEFAULT_LIMIT) Integer limit,
			@RequestParam(defaultValue = DEFAULT_OFFSET) Integer offset) {
	
		return batchRepository.findAll(toPageRequest(offset, limit));
	}


}
