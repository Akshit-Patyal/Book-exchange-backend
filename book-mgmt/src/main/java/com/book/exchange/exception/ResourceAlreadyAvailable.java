package com.book.exchange.exception;
public class ResourceAlreadyAvailable extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1790176767560489994L;

	public ResourceAlreadyAvailable(String message) {
		super(message);
	}
}