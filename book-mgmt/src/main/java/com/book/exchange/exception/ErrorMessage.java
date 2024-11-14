package com.book.exchange.exception;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {

	public int code;
	
	public String status;

	public String message;
	
	public String path;

	public LocalDateTime timeStamp;
}