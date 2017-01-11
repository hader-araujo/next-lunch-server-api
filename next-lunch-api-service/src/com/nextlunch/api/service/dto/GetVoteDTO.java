package com.nextlunch.api.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nextlunch.api.entity.Vote;

public class GetVoteDTO implements GenenricDTO {

	@JsonIgnore
	private static final long serialVersionUID = 4824005606745947284L;

	@NotNull
	private Long userId;
	@Null
	private Date day;

	public GetVoteDTO() {
	}

	public GetVoteDTO(Vote vote) {
		this.setUserId(vote.getUser().getId());
		this.setDay(vote.getDay());
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((day == null) ? 0 : day.hashCode());
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
		GetVoteDTO other = (GetVoteDTO) obj;
		if (day == null) {
			if (other.day != null)
				return false;
		} else if (!day.equals(other.day))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GetVoteDTO [userId=" + userId + ", day=" + day + "]";
	}
}
