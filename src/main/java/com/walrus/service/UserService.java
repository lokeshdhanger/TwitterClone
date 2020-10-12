package com.walrus.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

public interface UserService {
	
	public ResponseEntity<?> searchUserHandle(String username, HttpServletRequest request, HttpServletResponse response);

	public ResponseEntity<?> followUserHandle(String username, HttpServletRequest request,
			HttpServletResponse response);

}
