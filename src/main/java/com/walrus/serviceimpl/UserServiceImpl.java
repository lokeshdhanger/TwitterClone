package com.walrus.serviceimpl;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.walrus.bo.FollowResponseBO;
import com.walrus.bo.ResponseBO;
import com.walrus.bo.UserSearchResponseBO;
import com.walrus.entity.FollowEntity;
import com.walrus.exception.WalrusException;
import com.walrus.repository.FollowRepository;
import com.walrus.repository.UserRepository;
import com.walrus.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FollowRepository followRepository;

	@Override
	public ResponseEntity<?> searchUserHandle(String username, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("searchUserHandle() :: called with username = {} ", username);
		
		if (StringUtils.isBlank(username)) {
			throw new WalrusException("user handle should not be null or empty ", HttpStatus.NOT_FOUND);
		}
		
		UserSearchResponseBO userSearchResponseBO = userRepository.findByUsername(username);
		if (null!=userSearchResponseBO) {
			log.info("searchUserHandle() :: user handle found with username = {} ", username);
			return ResponseEntity.ok(userSearchResponseBO);
		}

		log.info("searchUserHandle() :: no handle found with username = {} ", username);
		ResponseEntity.of(Optional.of(new ResponseBO("no handle found with username = "+username, HttpStatus.NOT_FOUND)));
		return ResponseEntity.notFound().build();
	}

	@Override
	public ResponseEntity<?> followUserHandle(String username, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("followUserHandle() :: called with username = {} ", username);
		Long userId = (Long)request.getSession().getAttribute("userId");
		if (userId==null) {
			log.error("user is not logged in, unauthorized request");
			throw new WalrusException("You are not logged in, Please login !", HttpStatus.UNAUTHORIZED);
		 }	
		if (StringUtils.isBlank(username)) {
			log.warn("search user handle is either empty or null username = {}", username);
			throw new WalrusException("user handle should not be null or empty ", HttpStatus.NOT_FOUND);
		}
			
		UserSearchResponseBO userSearchResponseBO = userRepository.findByUsername(username);
		if (null!=userSearchResponseBO) {
			log.info("followUserHandle() :: user handle found with username = {} ", username);
			
			if (userSearchResponseBO.getId().equals(userId)) {
				log.error("user cannot follow self");
				throw new WalrusException("you cannot follow yourself", HttpStatus.BAD_REQUEST);
			}
			
			//check logged in user already following the another handle ?
			FollowResponseBO doesUserFollowUsernameHandle = followRepository.doesUserFollowUsernameHandle(userSearchResponseBO.getId(), userId);
			FollowEntity followEntity = prepareFollowEntity(userSearchResponseBO, userId);
			if (doesUserFollowUsernameHandle==null) {
				//no record exists, follow user handle, create new record
				FollowEntity followSaved = followRepository.save(followEntity);				
				if (followSaved!=null) {
					log.info("user = {} started following {}", userId, username);
					return ResponseEntity.ok(new ResponseBO("you started following "+username, HttpStatus.OK));
				} else {
					log.error("something went wrong while following user");
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseBO("something went wrong while following the user handle", HttpStatus.OK));		
				}
			} else if(!doesUserFollowUsernameHandle.getIsFollowed()){
				//record exist, follow user handle
				log.info("followUserHandle() :: user {} started following with username = {} ", userId, username);
				followRepository.updateFollowForUserHandle(doesUserFollowUsernameHandle.getId(), Boolean.valueOf(true));
				return ResponseEntity.ok(new ResponseBO("you started following "+username, HttpStatus.OK));
			} else {
				//unfollow user
				log.info("followUserHandle() :: user {} unfollowed handle @{}  ",userId, username);
				followRepository.updateFollowForUserHandle(doesUserFollowUsernameHandle.getId(), Boolean.valueOf(false));
				return ResponseEntity.ok(new ResponseBO("you unfollowed "+username, HttpStatus.NOT_FOUND));
			}
		}
		log.warn("followUserHandle() :: no user handle found with username = {} ", username);
		return ResponseEntity.ok(new ResponseBO("no user handle found for "+username, HttpStatus.NOT_FOUND));
	}


	private FollowEntity prepareFollowEntity(UserSearchResponseBO userSearchResponseBO, Long userId) {
		log.debug("prepareFollowEntity() :: called with userSearchResponseBO = {} userId ={}", userSearchResponseBO, userId);
		FollowEntity followEntity = new FollowEntity();
		followEntity.setCreatedAt(new Date());
		followEntity.setCreatedBy(userId);
		followEntity.setIsFollowed(Boolean.valueOf(true));
		followEntity.setFollowerUserId(userId);
		followEntity.setUpdatedAt(new Date());
		followEntity.setUpdatedBy(userId);
		followEntity.setUserId(userSearchResponseBO.getId());
		return followEntity;
	}

}
