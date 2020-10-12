package com.walrus.bo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostRequestBO extends AbstractBO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NonNull
	private String post;

}
