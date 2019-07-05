package com.armandorvila.poc.price.resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.armandorvila.poc.price.domain.Price;
import com.armandorvila.poc.price.repository.PriceRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PriceResource {

	private static final String DEFAULT_OFFSET = "0";
	private static final String DEFAULT_LIMIT = "100";

	private PriceRepository accountRepository;

	public PriceResource(PriceRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@GetMapping("/prices")
	public Flux<Price> getPrices(
			@RequestParam(defaultValue = DEFAULT_LIMIT) Integer limit,
			@RequestParam(defaultValue = DEFAULT_OFFSET) Integer offset,
			@RequestParam(required = true) String instrumentId) {
	
		return accountRepository.findByInstrumentId(instrumentId, toPageRequest(offset, limit));
	}
	
	@GetMapping("/prices/last")
	public Mono<Price> getLastPriceByInstrumentId(@RequestParam(required = true) String instrumentId) {
		return accountRepository.findTopByInstrumentIdOrderByAsOfDesc(instrumentId);
	}

	private Pageable toPageRequest(int offset, int limit) {
		int page = offset >= limit ? offset / limit : 0;
		return PageRequest.of(page, limit);
	}
}
