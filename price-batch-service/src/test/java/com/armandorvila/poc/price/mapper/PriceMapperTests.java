package com.armandorvila.poc.price.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.time.format.DateTimeParseException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.armandorvila.poc.price.domain.Price;

public class PriceMapperTests {

	private static final String INSTRUMENT_ID = "8022dc07-77cc-4cea-b473-c1160c54c487";
	private static final String AS_OF = "2018-08-11 02:08:33";
	private static final String PAYLOAD = "€9683,08";

	private PriceMapper mapper = new PriceMapper();

	private FieldSet fieldSet;

	@Before
	public void setUp() {
		fieldSet = mock(FieldSet.class);
	}

	@Test
	public void should_MapAllTheFields_When_GivenValidData() throws BindException {
		
		given(fieldSet.readRawString("instrumentId")).willReturn(INSTRUMENT_ID);
		given(fieldSet.readRawString("payload")).willReturn(PAYLOAD);
		given(fieldSet.readRawString("asOf")).willReturn(AS_OF);

		Price price = mapper.mapFieldSet(fieldSet);
		
		then(fieldSet).should(times(1)).readRawString("instrumentId");
		then(fieldSet).should(times(1)).readRawString("payload");
		then(fieldSet).should(times(1)).readRawString("asOf");

		assertThat(price).isNotNull();
		assertThat(price.getInstrumentId()).isEqualTo(INSTRUMENT_ID);
		assertThat(price.getPayload()).isEqualTo(PAYLOAD);
	}
	
	@Test(expected = DateTimeParseException.class)
	public void should_ThrowDateTimeParseException_When_GivenInvalidDate() throws BindException {
		
		given(fieldSet.readRawString("instrumentId")).willReturn(INSTRUMENT_ID);
		given(fieldSet.readRawString("payload")).willReturn(PAYLOAD);
		given(fieldSet.readRawString("asOf")).willReturn("Not a date");
		
		mapper.mapFieldSet(fieldSet);
		
		then(fieldSet).should(times(1)).readRawString("instrumentId");
		then(fieldSet).should(times(1)).readRawString("payload");
		then(fieldSet).should(times(1)).readRawString("asOf");
	}
	
}
