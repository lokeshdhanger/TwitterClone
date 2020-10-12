package com.walrus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.walrus.bo.PostResponseBO;
import com.walrus.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long>{

	@Query("select new com.walrus.bo.PostResponseBO(prb.id, prb.post, prb.userId, prb.createdAt, prb.updatedAt) from posts prb where prb.userId = :userId")
	List<PostResponseBO> findAllPostByUserId(Long userId);

	@Query("select new com.walrus.bo.PostResponseBO(prb.id, prb.post, prb.userId, prb.createdAt, prb.updatedAt) from posts prb where prb.userId in (:followersId)")
	List<PostResponseBO> findAllByUserId(List<Long> followersId);

}
