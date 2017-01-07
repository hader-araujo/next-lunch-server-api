package com.nextlunch.api.rest.controller;

import org.springframework.http.ResponseEntity;

public interface RestaurantRestController {

	@SuppressWarnings("rawtypes")
	ResponseEntity getAll();
}