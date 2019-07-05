package com.armandorvila.poc.price.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

public class BadRecordSkipListener implements SkipListener<Object, Object>{

	private static final Logger logger = LoggerFactory.getLogger(BadRecordSkipListener.class);
	
	@Override
	public void onSkipInRead(Throwable t) {
		logger.error("Error reading item", t);
	}

	@Override
	public void onSkipInWrite(Object item, Throwable t) {
		logger.error("Error writing item", t);
	}

	@Override
	public void onSkipInProcess(Object item, Throwable t) {
		logger.error("Error processing item", t);
	}

}
