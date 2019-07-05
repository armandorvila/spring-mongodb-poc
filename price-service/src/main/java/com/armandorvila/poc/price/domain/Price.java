package com.armandorvila.poc.price.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "prices")
@CompoundIndex(background = true, def = "{'instrumentId' : 1, 'asOf' : -1}")
public class Price {

	@Id
	private String id;

	@NotEmpty(message = "The payload field is required")
	private String payload;

	@NotEmpty(message = "The payload field is required")
	private String instrumentId;

	@NotNull(message = "The asOf field is required")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDateTime asOf;
}
