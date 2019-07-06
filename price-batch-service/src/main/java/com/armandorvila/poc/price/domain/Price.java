package com.armandorvila.poc.price.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"createdAt"})
@Document(collection = "prices")
public class Price {
	
	@Id
	private String id;
	
	private String payload;
	
	private String instrumentId;
	
	private String batchId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDateTime asOf;
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDateTime createdAt;
}
