package com.book.exchange.exception;

public class InvalidTokenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7519468377529363870L;

	public InvalidTokenException(String message) {
		super(message);
	}
}
