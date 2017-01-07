package com.nextlunch.api.rest.controller;

import org.springframework.http.ResponseEntity;

public interface UserRestController {

	@SuppressWarnings("rawtypes")
	ResponseEntity getAll();
}