package com.walrus.bo;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class UserRegistrationRequestBO extends AbstractBO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NonNull
	private String username;
	
	@NonNull
	private String email;
	
	@NonNull
	private String password;
	
	@NonNull
	private String reTypedPassword;
	
	@NonNull
	private boolean termsAndConditionsAccept;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((reTypedPassword == null) ? 0 : reTypedPassword.hashCode());
		result = prime * result + (termsAndConditionsAccept ? 1231 : 1237);
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRegistrationRequestBO other = (UserRegistrationRequestBO) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (reTypedPassword == null) {
			if (other.reTypedPassword != null)
				return false;
		} else if (!reTypedPassword.equals(other.reTypedPassword))
			return false;
		if (termsAndConditionsAccept != other.termsAndConditionsAccept)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
	
	

}
