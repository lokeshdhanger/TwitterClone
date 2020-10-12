package com.walrus.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class WalrusExceptionHandler {
	
	@ExceptionHandler(WalrusException.class)
	public ResponseEntity<String> handleException(WalrusException walrusException) {
		log.info("handleException() :: called with walrusException = {}");
		return new ResponseEntity<String>(walrusException.getMessage(), walrusException.getHttpStatus());
	}

}
