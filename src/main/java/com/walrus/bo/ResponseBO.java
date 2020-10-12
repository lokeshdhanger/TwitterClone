package com.walrus.bo;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ResponseBO extends AbstractBO{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	private HttpStatus httpStatus;
	
	
	public ResponseBO(String message, HttpStatus httpStatus) {
		this.message=message;
		this.httpStatus=httpStatus;
	}
	

}
