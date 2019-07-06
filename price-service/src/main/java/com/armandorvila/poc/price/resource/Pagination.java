package com.armandorvila.poc.price.resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Pagination {

	public static final String DEFAULT_OFFSET = "0";
	public static final String DEFAULT_LIMIT = "100";

	private Pagination() {
	}

	public static Pageable toPageRequest(int offset, int limit) {
		int page = offset >= limit ? offset / limit : 0;
		return PageRequest.of(page, limit);
	}
}
