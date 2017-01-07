package com.nextlunch.api.service;

import java.util.List;

import com.nextlunch.api.service.dto.RestaurantDTO;
import com.nextlunch.api.service.exception.ReadException;

public interface RestaurantService {

	List<RestaurantDTO> getAll() throws ReadException;

	RestaurantDTO findOne(Long id) throws ReadException;

}