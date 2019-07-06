package com.armandorvila.poc.price.resource;

import static com.armandorvila.poc.price.resource.Pagination.DEFAULT_LIMIT;
import static com.armandorvila.poc.price.resource.Pagination.DEFAULT_OFFSET;
import static com.armandorvila.poc.price.resource.Pagination.toPageRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.armandorvila.poc.price.domain.BatchState;
import com.armandorvila.poc.price.domain.Price;
import com.armandorvila.poc.price.repository.BatchRepository;
import com.armandorvila.poc.price.repository.PriceRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class PriceResource {

	private PriceRepository priceRepository;

	private BatchRepository batchRepository;

	public PriceResource(PriceRepository priceRepository, BatchRepository batchRepository) {
		this.priceRepository = priceRepository;
		this.batchRepository = batchRepository;
	}

	@GetMapping("/prices")
	public Flux<Price> getPrices(@RequestParam(defaultValue = DEFAULT_LIMIT) Integer limit,
			@RequestParam(defaultValue = DEFAULT_OFFSET) Integer offset,
			@RequestParam(required = true) String instrumentId) {
		
		Flux<Price> prices = priceRepository.findByInstrumentIdAndBatchIdNotIn(instrumentId, getInProgressBatchIds(),
				toPageRequest(offset, limit));

		return prices;
	}

	@GetMapping("/prices/last")
	public Mono<Price> getLastPriceByInstrumentId(@RequestParam(required = true) String instrumentId) {
		return priceRepository.findTopByInstrumentIdAndBatchIdNotInOrderByAsOfDesc(instrumentId, getInProgressBatchIds());
	}
	
	private Flux<String> getInProgressBatchIds() {
		return batchRepository.findByState(BatchState.IN_PROGRESS)
				.map(b -> b.getId());
	}
}
