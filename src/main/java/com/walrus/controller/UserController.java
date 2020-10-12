package com.walrus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walrus.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//search for user
	@GetMapping("search")
	public ResponseEntity<?> searchUserHandle(@RequestParam(name = "username", required = true) String username, HttpServletRequest request, HttpServletResponse response){
		log.info("searchUserHandle() :: called  username = {}", username);
		return userService.searchUserHandle(username, request, response);
	}
	
	
	//follow a user
	@PutMapping("follow")
	public ResponseEntity<?> followUserHandle(@RequestParam(name = "username", required = true) String username, HttpServletRequest request, HttpServletResponse response){
		log.info("followUserHandle() :: called  username = {}", username);
		return userService.followUserHandle(username, request, response);
	}
	
	//

}
