package com.nextlunch.api.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nextlunch.api.entity.Vote;

public class VoteDTO implements GenenricDTO {

	@JsonIgnore
	private static final long serialVersionUID = 4824005606745947284L;

	@Null
	private Long id;
	@NotNull
	private Long restaurantId;
	@NotNull
	private Long userId;
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date day;

	public VoteDTO() {
	}

	public VoteDTO(Vote vote) {
		this.setId(vote.getId());
		this.setRestaurantId(vote.getRestaurant().getId());
		this.setUserId(vote.getUser().getId());
		this.setDay(vote.getDay());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((restaurantId == null) ? 0 : restaurantId.hashCode());
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
		VoteDTO other = (VoteDTO) obj;
		if (day == null) {
			if (other.day != null)
				return false;
		} else if (!day.equals(other.day))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (restaurantId == null) {
			if (other.restaurantId != null)
				return false;
		} else if (!restaurantId.equals(other.restaurantId))
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
		return "VoteDTO [id=" + id + ", restaurantId=" + restaurantId + ", userId=" + userId + ", day=" + day + "]";
	}
}
