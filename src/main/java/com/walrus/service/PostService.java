package com.walrus.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.walrus.bo.PostRequestBO;

public interface PostService {
	
	public ResponseEntity<?> createPost(PostRequestBO postRequestBO, HttpServletRequest request, HttpServletResponse response);

	public ResponseEntity<?> updatePostLike(Long postId, HttpServletRequest request, HttpServletResponse response);

	public ResponseEntity<?> getAllPostsOfAUser(HttpServletRequest request, HttpServletResponse response);

	public ResponseEntity<?> getFeed(HttpServletRequest request, HttpServletResponse response);

}
