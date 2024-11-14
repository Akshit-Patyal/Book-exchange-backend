package com.book.exchange.exception;
public class InvalidCredentials extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6541779664050464L;

	public InvalidCredentials(String message) {
		super(message);
	}
}