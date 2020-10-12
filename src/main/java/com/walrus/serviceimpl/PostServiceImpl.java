package com.walrus.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.walrus.bo.FollowResponseBO;
import com.walrus.bo.PostLikeResponseBO;
import com.walrus.bo.PostRequestBO;
import com.walrus.bo.PostResponseBO;
import com.walrus.bo.ResponseBO;
import com.walrus.entity.PostEntity;
import com.walrus.entity.PostLikeEntity;
import com.walrus.exception.WalrusException;
import com.walrus.repository.FollowRepository;
import com.walrus.repository.PostLikesRepository;
import com.walrus.repository.PostRepository;
import com.walrus.repository.UserRepository;
import com.walrus.service.PostService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private PostLikesRepository postLikesRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FollowRepository followRepository;
	
	@Override
	public ResponseEntity<?> createPost(PostRequestBO postRequestBO, HttpServletRequest request, HttpServletResponse response) {
		log.info("createPost() :: called with postRequestBO = {}", postRequestBO);
		if (StringUtils.isBlank(postRequestBO.getPost())) {
			throw new WalrusException("post content cannot be null", HttpStatus.NO_CONTENT);
		}
				
		Long userId = (Long) request.getSession().getAttribute("userId");
		if (userId==null) {
			log.error("createPost() :: user id not found !");
			throw new WalrusException("user is not logged in", HttpStatus.UNAUTHORIZED);
		}
		
		PostEntity postEntity = preparePostEntity(postRequestBO, userId);
		PostEntity postEntityResponse = postRepository.save(postEntity);
		
		if (postEntityResponse!=null) {
			log.info("createPost() :: post created successfully !");
			return ResponseEntity.ok(new ResponseBO("Post created successfully !", HttpStatus.OK));
		}
		log.warn("createPost() ::  unable to create post.");
		return ResponseEntity.ok(new ResponseBO("Oops ! something went wrong", HttpStatus.INTERNAL_SERVER_ERROR));
	}

	private PostEntity preparePostEntity(PostRequestBO postRequestBO, Long userId) {
		log.debug("preparePostEntity() :: called with postRequestBO = {} userId = {}", postRequestBO, userId);
		PostEntity postEntity = new PostEntity();
		postEntity.setPost(postRequestBO.getPost());
		postEntity.setUserId(userId);
		postEntity.setCreatedAt(new Date());
		postEntity.setCreatedBy(userId);
		postEntity.setUpdatedAt(new Date());
		postEntity.setUpdatedBy(userId);
		return postEntity;
	}

	@Override
	public ResponseEntity<?> updatePostLike(Long postId, HttpServletRequest request, HttpServletResponse response) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		if (userId==null) {
			log.error("updatePostLike() :: user id not found !");
			throw new WalrusException("user is not logged in", HttpStatus.UNAUTHORIZED);
		}
		
		PostLikeEntity postlikeEntity = postLikesRepository.isLikeExistOfUserForPost(postId, userId);
		if (postlikeEntity!=null) {
			if (postlikeEntity.isLiked()) {
				postLikesRepository.updateLikeForPost(postlikeEntity.getId(), !postlikeEntity.isLiked());
				log.info("updatePostLike() :: user = {} disliked this post", userId);
				return ResponseEntity.ok(new ResponseBO("you disliked this post", HttpStatus.OK));
			}
			postLikesRepository.updateLikeForPost(postlikeEntity.getId(), !postlikeEntity.isLiked());
			log.info("updatePostLike() :: user = {} liked this post", userId);
			return ResponseEntity.ok(new ResponseBO("you liked this post", HttpStatus.OK));
		}
		
		PostLikeEntity postLikeEntity = preparePostLikeEntity(postId, userId);
		PostLikeEntity likeEntity = postLikesRepository.save(postLikeEntity);
		
		if (likeEntity!=null) {
			log.info("updatePostLike() :: like updated successfully");
			return ResponseEntity.ok(new ResponseBO("you liked this post", HttpStatus.OK));
		}
		return ResponseEntity.ok(new ResponseBO("something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR));
	}

	private PostLikeEntity preparePostLikeEntity(Long postId, Long userId) {
		log.debug("preparePostLikeEntity() :: called with postId = {} userId = {}", postId, userId);
		PostLikeEntity postLikeEntity = new PostLikeEntity();
		postLikeEntity.setCreatedAt(new Date());
		postLikeEntity.setLiked(true);
		postLikeEntity.setCreatedBy(userId);
		postLikeEntity.setPostId(postId);
		postLikeEntity.setUpdatedAt(new Date());
		postLikeEntity.setUpdatedBy(userId);
		postLikeEntity.setUserId(userId);
		return postLikeEntity;
	}

	@Override
	public ResponseEntity<?> getAllPostsOfAUser(HttpServletRequest request, HttpServletResponse response) {
		log.info("getAllPostsOfAUser() :: called ");

		Long userId = (Long) request.getSession().getAttribute("userId");
		if (userId==null) {
			log.error("getAllPostsOfAUser() :: user id not found !");
			throw new WalrusException("user is not logged in", HttpStatus.UNAUTHORIZED);
		}
		
		List<PostResponseBO> postResponseBOList = postRepository.findAllPostByUserId(userId);
		
		preparePostsAndLikes(postResponseBOList);
		
		return ResponseEntity.ok(postResponseBOList);
	}

	private void preparePostsAndLikes(List<PostResponseBO> postResponseBOList) {
		if (postResponseBOList==null || postResponseBOList.isEmpty()) {
			log.debug("preparePostsAndLikes() :: no post published postResponseBOList = {}",postResponseBOList);
			throw new WalrusException("no post published", HttpStatus.NOT_FOUND);
		}
		
		List<Long> postIdList = postResponseBOList.stream().mapToLong(PostResponseBO::getId).boxed().collect(Collectors.toList());
		List<PostLikeResponseBO> totalLikesOfPosts = postLikesRepository.getTotalLikesOfPosts(postIdList);

		for (PostResponseBO postResponseBO : postResponseBOList) {
			for (PostLikeResponseBO postLikeResponseBO : totalLikesOfPosts) {
				if (postResponseBO.getId().equals(postLikeResponseBO.getPostId())) {
					postResponseBO.setLikes(postLikeResponseBO.getLikesCount());
				}
			}
		}
	}

	@Override
	public ResponseEntity<?> getFeed(HttpServletRequest request, HttpServletResponse response) {
		log.info("getFeed() :: called ");
		
		Long userId = (Long) request.getSession().getAttribute("userId");
		if (userId==null) {
			log.error("getFeed() :: user not logged in, unauthorized request !");
			throw new WalrusException("you are not logged in, Please login again !", HttpStatus.UNAUTHORIZED);
		}
		
		List<FollowResponseBO> following = followRepository.userFollowedHandles(userId, Boolean.valueOf(true));
		List<PostResponseBO> posts = null;
		if (following!=null && !following.isEmpty()) {
			List<Long> followersId = following.stream().mapToLong(FollowResponseBO::getUserId).boxed().collect(Collectors.toList());
			posts = postRepository.findAllByUserId(followersId);
			preparePostsAndLikes(posts);
			log.error("getFeed() :: total posts = {} found ", following.size());
			return ResponseEntity.ok(posts);
		}
		log.error("getFeed() :: not post found");
		return ResponseEntity.ok(new ResponseBO("no posts found !", HttpStatus.NOT_FOUND));
	}

}
