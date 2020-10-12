package com.walrus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.walrus.bo.LoginResponseBO;
import com.walrus.bo.UserSearchResponseBO;
import com.walrus.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

	@Query("select username from users where username = :username")
	String userExistsByUsername(String username);
	
	@Query("select email from users where email = :email")
	String userExistsByEmail(String email);

	@Query("select new com.walrus.bo.LoginResponseBO(u.id, u.username, u.email, u.createdAt, u.updatedAt) from users u where u.username = :username and u.password = :password")
	LoginResponseBO findByUsernameAndPassword(String username, String password);

	@Query("select new com.walrus.bo.UserSearchResponseBO(u.id, u.username, u.email) from users u where u.username = :username")
	UserSearchResponseBO findByUsername(String username);

}
