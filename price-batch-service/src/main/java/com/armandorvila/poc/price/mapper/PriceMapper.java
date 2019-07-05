package com.armandorvila.poc.price.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.armandorvila.poc.price.domain.Price;

public class PriceMapper extends BeanWrapperFieldSetMapper<Price> {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
	
	 @Override
	    public Price mapFieldSet(FieldSet fieldSet) throws BindException {
		 Price price = new Price();
		 
		 LocalDateTime.parse("2018-10-16T08:00:00");
		 
		 price.setInstrumentId(fieldSet.readRawString("id"));
		 price.setPayload(fieldSet.readRawString("payload"));

		 price.setAsOf(LocalDateTime.parse(fieldSet.readRawString("asOf"), FORMATTER));
		 
		 return price;
		 
	 }
}
