package com.walrus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.walrus.bo.UserLoginRequestBO;
import com.walrus.bo.UserRegistrationRequestBO;
import com.walrus.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("register")
	public ResponseEntity<?> registration(@RequestBody(required = true) UserRegistrationRequestBO userRegistrationBO) {
		log.info("registration() :: called with userRegistrationBO = {}", userRegistrationBO);
		return authService.registerUser(userRegistrationBO);
	}
	
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody(required = true) UserLoginRequestBO userLoginRequestBO, HttpServletRequest request, HttpServletResponse response) {
		log.info("login() :: called with userLoginRequestBO = {}", userLoginRequestBO);
		return authService.loginUser(userLoginRequestBO, request, response);
	}
	
	@GetMapping("logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		log.info("logout() :: called ");
		return authService.logoutUser(request, response);
	}

}
