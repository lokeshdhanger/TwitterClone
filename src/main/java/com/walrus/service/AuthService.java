package com.walrus.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.walrus.bo.UserLoginRequestBO;
import com.walrus.bo.UserRegistrationRequestBO;

public interface AuthService {

	ResponseEntity<?> registerUser(UserRegistrationRequestBO userRegistrationBO);

	ResponseEntity<?> loginUser(UserLoginRequestBO userLoginRequestBO, HttpServletRequest request, HttpServletResponse response);

	ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response);
	
	

}
