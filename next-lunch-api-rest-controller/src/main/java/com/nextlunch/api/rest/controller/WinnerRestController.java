package com.nextlunch.api.rest.controller;

import java.util.Date;

import org.springframework.http.ResponseEntity;

public interface WinnerRestController {

	@SuppressWarnings("rawtypes")
	ResponseEntity getWinnerOfDay(Date day);
	
	@SuppressWarnings("rawtypes")
	ResponseEntity getWinnersOfWeek(Date day);
	
}