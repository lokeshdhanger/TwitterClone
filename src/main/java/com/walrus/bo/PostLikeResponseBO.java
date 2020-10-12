package com.walrus.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeResponseBO extends AbstractBO{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long postId;
	private Long likesCount;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((likesCount == null) ? 0 : likesCount.hashCode());
		result = prime * result + ((postId == null) ? 0 : postId.hashCode());
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
		PostLikeResponseBO other = (PostLikeResponseBO) obj;
		if (likesCount == null) {
			if (other.likesCount != null)
				return false;
		} else if (!likesCount.equals(other.likesCount))
			return false;
		if (postId == null) {
			if (other.postId != null)
				return false;
		} else if (!postId.equals(other.postId))
			return false;
		return true;
	}
	
	

}
