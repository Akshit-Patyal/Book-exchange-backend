package com.book.exchange.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookRequest {

	private Long userId;
	private String title;
	private String author;
	private String genre;
	private String condition;
	private String location;
	private boolean isAvailable;
	
}
