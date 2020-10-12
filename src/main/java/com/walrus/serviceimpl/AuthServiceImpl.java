package com.walrus.serviceimpl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.walrus.bo.LoginResponseBO;
import com.walrus.bo.ResponseBO;
import com.walrus.bo.UserLoginRequestBO;
import com.walrus.bo.UserRegistrationRequestBO;
import com.walrus.entity.UserEntity;
import com.walrus.exception.WalrusException;
import com.walrus.repository.UserRepository;
import com.walrus.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private UserRepository userRepository; 

	@Override
	public ResponseEntity<?> registerUser(UserRegistrationRequestBO userRegistrationRequestBO) {
		log.info("registerUser() :: called with userRegistrationRequestBO = {} ", userRegistrationRequestBO);
		
		if (!userRegistrationRequestBO.isTermsAndConditionsAccept()) {
			log.error("registerUser() :: terms and conditions are not accepted ! ");
			String message = "please, accept terms and conditions";
			throw new WalrusException(message, HttpStatus.BAD_REQUEST);
		}
		
		String existingUsername = userRepository.userExistsByUsername(userRegistrationRequestBO.getUsername());
		if (StringUtils.isNotBlank(existingUsername)) {
			log.error("registerUser() :: username already exists ! ");
			String message = "username has been taken, please select another username !";
			throw new WalrusException(message, HttpStatus.CONFLICT);
		}
		
		String existingEmail = userRepository.userExistsByUsername(userRegistrationRequestBO.getEmail());
		if (StringUtils.isNotBlank(existingEmail)) {
			log.error("registerUser() :: email already exists ! ");
			String message = "email has been taken, please select another email !";
			throw new WalrusException(message, HttpStatus.CONFLICT);
		}
		
		if (!StringUtils.equals(userRegistrationRequestBO.getPassword(), userRegistrationRequestBO.getReTypedPassword())) {
			log.error("registerUser() :: passwords do not match ! ");
			String message = "both password do not match !";
			throw new WalrusException(message, HttpStatus.NOT_ACCEPTABLE);
		}
		
		
		UserEntity userEntity = prepareUserEntity(userRegistrationRequestBO);
		UserEntity save = userRepository.save(userEntity);
		if (save!=null) {			
			log.info("registerUser() :: user registration successfull ! ");
			return ResponseEntity.ok(new ResponseBO("Your profile created successfully !", HttpStatus.OK));
		}
		
		log.warn("registerUser() :: Unable to register the user ! ");
		return ResponseEntity.badRequest().body(new ResponseBO("Oops ! something went wrong", HttpStatus.BAD_REQUEST));
	}

	private UserEntity prepareUserEntity(UserRegistrationRequestBO userRegistrationRequestBO) {
		log.debug("prepareUserEntity() :: called with userRegistrationRequestBO = {}", userRegistrationRequestBO);
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(userRegistrationRequestBO.getEmail());
		userEntity.setUsername(userRegistrationRequestBO.getUsername());
		userEntity.setPassword(userRegistrationRequestBO.getPassword());
		userEntity.setCreatedAt(new Date());
		userEntity.setCreatedBy(1L); //default
		userEntity.setUpdatedAt(new Date());
		userEntity.setUpdatedBy(1L); //default
		userEntity.setTermsAndConditionsAccept(userRegistrationRequestBO.isTermsAndConditionsAccept());
		return userEntity;
	}

	@Override
	public ResponseEntity<?> loginUser(UserLoginRequestBO userLoginRequestBO, HttpServletRequest request, HttpServletResponse response) {
		log.info("loginUser() :: called with userLoginRequestBO = {}", userLoginRequestBO);
		
		String userExistsByUsername = userRepository.userExistsByUsername(userLoginRequestBO.getUsername());
		
		if (StringUtils.isBlank(userExistsByUsername)) {
			log.error("loginUser() :: user with username = {} does not exist !", userLoginRequestBO.getUsername());
			throw new WalrusException("user does not existing by username "+userLoginRequestBO.getUsername(), HttpStatus.NOT_FOUND);
		}
		
		LoginResponseBO loginResponseBO = userRepository.findByUsernameAndPassword(userLoginRequestBO.getUsername(), userLoginRequestBO.getPassword());
		if (loginResponseBO==null) {
			log.error("loginUser() :: invalid credentials !");
			throw new WalrusException("invalid credentials !", HttpStatus.UNAUTHORIZED);
		}
		log.info("loginUser() :: login succesfully !");
		request.getSession().setAttribute("userId", loginResponseBO.getId());
		return ResponseEntity.ok(loginResponseBO);
	}

	@Override
	public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
		log.info("logoutUser() :: called ");
		 Long userId = (Long)request.getSession().getAttribute("userId");
		 if (userId!=null) {
				request.getSession().removeAttribute("userId");
				log.info("logoutUser() :: user = {} logged out successfully .", userId);
				return ResponseEntity.ok(new ResponseBO("logout successfully !", HttpStatus.OK));			
		}
		log.info("logoutUser() :: user is not logged in.");
		return ResponseEntity.badRequest().body(new ResponseBO("user are not logged in !", HttpStatus.UNPROCESSABLE_ENTITY));			

	}
	
	

}
