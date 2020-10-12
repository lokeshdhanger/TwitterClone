package com.walrus.bo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Setter
@Getter
public class FollowResponseBO extends AbstractBO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long userId;
	private Long followerUserId;
	private Boolean isFollowed;
	
	public FollowResponseBO(Long id, Long userId, Long followerUserId, Boolean isFollowed) {
		this.id=id;
		this.userId=userId;
		this.followerUserId=followerUserId;
		this.isFollowed=isFollowed;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((followerUserId == null) ? 0 : followerUserId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		FollowResponseBO other = (FollowResponseBO) obj;
		if (followerUserId == null) {
			if (other.followerUserId != null)
				return false;
		} else if (!followerUserId.equals(other.followerUserId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	

	
}
