package com.armandorvila.poc.price.domain;

import java.time.LocalDateTime;

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
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt"})
@Document(collection = "batches")
public class Batch {

	@Id
	private String id;
	
	private String dataFile;
	
	private BatchState state;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDateTime createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDateTime updatedAt;
	
	public Batch(String id, String dataFile, BatchState state) {
		this.id = id;
		this.dataFile = dataFile;
		this.state = state;
	}
}
