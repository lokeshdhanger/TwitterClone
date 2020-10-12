package com.walrus.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.walrus.bo.PostLikeResponseBO;
import com.walrus.entity.PostLikeEntity;

@Repository
public interface PostLikesRepository extends JpaRepository<PostLikeEntity, Long> {

	@Query("select ple from post_likes ple where ple.postId = :postId and ple.userId = :userId")
	PostLikeEntity isLikeExistOfUserForPost(Long postId, Long userId);

	@Transactional
	@Modifying
	@Query("update post_likes set is_liked = :is_liked where id = :id")
	void updateLikeForPost(Long id, boolean is_liked);

	@Query("select new com.walrus.bo.PostLikeResponseBO(postId, count(*) as likesCount) from post_likes where post_id in (:postIdList) group by postId")
	List<PostLikeResponseBO> getTotalLikesOfPosts(List<Long> postIdList);

}
