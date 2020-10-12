package com.walrus.exception;

import org.springframework.http.HttpStatus;

import lombok.ToString;

@ToString
public class WalrusException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	private HttpStatus httpStatus;
	
	public WalrusException(String message, HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}	
	

}
