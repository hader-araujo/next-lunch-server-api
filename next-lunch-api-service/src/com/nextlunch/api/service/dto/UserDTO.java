package com.nextlunch.api.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nextlunch.api.entity.User;

public class UserDTO implements GenenricDTO {

	@JsonIgnore
	private static final long serialVersionUID = 4824005606745947284L;

	private Long id;
	private String name;

	public UserDTO() {
	}

	public UserDTO(User user) {
		this.setId(user.getId());
		this.setName(user.getName());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		UserDTO other = (UserDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", name=" + name + "]";
	}
}
