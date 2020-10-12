package com.walrus.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.walrus.bo.FollowResponseBO;
import com.walrus.entity.FollowEntity;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long>{
	
	@Query("select new com.walrus.bo.FollowResponseBO(f.id, f.userId, f.followerUserId, f.isFollowed) from follows f where f.userId = :followedUser AND f.followerUserId = :followedBy")
	FollowResponseBO doesUserFollowUsernameHandle(Long followedUser, Long followedBy);
	
	
	@Modifying
	@Transactional
	@Query("update follows set is_followed = :follow where id = :id")
	void updateFollowForUserHandle(Long id, Boolean follow);


	@Query("select new com.walrus.bo.FollowResponseBO(f.id, f.userId, f.followerUserId, f.isFollowed) from follows f where f.followerUserId = :followerId and f.isFollowed = :isFollowed")
	List<FollowResponseBO> userFollowedHandles(Long followerId, Boolean isFollowed);
}
