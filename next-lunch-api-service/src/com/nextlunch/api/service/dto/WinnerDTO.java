package com.nextlunch.api.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WinnerDTO implements GenenricDTO {

	@JsonIgnore
	private static final long serialVersionUID = 4824005606745947284L;

	private Long restaurantId;
	private String restaurantName;
	private Long quantity;
	private boolean beforeMiddleDay;

	public WinnerDTO() {
	}

	public WinnerDTO(Long restaurantId, String restaurantName, Long quantity, boolean beforeMiddleDay) {
		this.restaurantId = restaurantId;
		this.restaurantName = restaurantName;
		this.quantity = quantity;
		this.beforeMiddleDay = beforeMiddleDay;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public boolean isBeforeMiddleDay() {
		return beforeMiddleDay;
	}

	public void setBeforeMiddleDay(boolean beforeMiddleDay) {
		this.beforeMiddleDay = beforeMiddleDay;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (beforeMiddleDay ? 1231 : 1237);
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((restaurantId == null) ? 0 : restaurantId.hashCode());
		result = prime * result + ((restaurantName == null) ? 0 : restaurantName.hashCode());
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
		WinnerDTO other = (WinnerDTO) obj;
		if (beforeMiddleDay != other.beforeMiddleDay)
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (restaurantId == null) {
			if (other.restaurantId != null)
				return false;
		} else if (!restaurantId.equals(other.restaurantId))
			return false;
		if (restaurantName == null) {
			if (other.restaurantName != null)
				return false;
		} else if (!restaurantName.equals(other.restaurantName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WinnerDTO [restaurantId=" + restaurantId + ", restaurantName=" + restaurantName + ", quantity="
				+ quantity + ", beforeMiddleDay=" + beforeMiddleDay + "]";
	}

}
