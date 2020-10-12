package com.walrus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walrus.bo.PostRequestBO;
import com.walrus.service.PostService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PostController {
	
	@Autowired
	private PostService postService;
	
	//create post
	@PostMapping("/posts")
	public ResponseEntity<?> createPost(@RequestBody(required = true) PostRequestBO postRequestBO, HttpServletRequest request, HttpServletResponse response){
		log.info("createPost() :: called with postRequestBO = {}", postRequestBO);
		return postService.createPost(postRequestBO, request, response);
	}
	
	//like a post
	@PutMapping("/posts/like")
	public ResponseEntity<?> likePost(@RequestParam(name = "postId", required = true ) Long postId, HttpServletRequest request, HttpServletResponse response){
		log.info("likePost() :: called postId = {}", postId);
		return postService.updatePostLike(postId, request, response);
	}
	
	//show feed
	@GetMapping("/feed")
	public ResponseEntity<?> getFeed(HttpServletRequest request, HttpServletResponse response){
		log.info("getFeed() :: called");
		return postService.getFeed(request, response);
	}
	
	//all posts of a user
	@GetMapping("/posts")
	public ResponseEntity<?> getAllPostsOfAUser(HttpServletRequest request, HttpServletResponse response){
		log.info("likePost() :: called");
		return postService.getAllPostsOfAUser(request, response);
	}
}
