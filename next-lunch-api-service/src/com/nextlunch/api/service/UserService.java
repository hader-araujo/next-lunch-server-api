package com.nextlunch.api.service;

import java.util.List;

import com.nextlunch.api.service.dto.UserDTO;
import com.nextlunch.api.service.exception.ReadException;

public interface UserService {

	List<UserDTO> getAll() throws ReadException;

	UserDTO findOne(Long id) throws ReadException;
}