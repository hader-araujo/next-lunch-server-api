package com.nextlunch.api.rest.controller;

import org.springframework.http.ResponseEntity;

public interface WinnerRestController {

	@SuppressWarnings("rawtypes")
	ResponseEntity getWinnerOfDay();
	
	@SuppressWarnings("rawtypes")
	ResponseEntity getWinnersOfWeek();
	
}