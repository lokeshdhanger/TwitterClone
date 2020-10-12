package com.walrus.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class UserSearchResponseBO extends AbstractBO{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String username;
	private String email;
	
	public UserSearchResponseBO(Long id, String username, String email) {
		this.id=id;
		this.username=username;
		this.email=email;
	}

}
