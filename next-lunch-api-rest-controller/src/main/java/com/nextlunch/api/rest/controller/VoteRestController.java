package com.nextlunch.api.rest.controller;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.nextlunch.api.service.dto.VoteDTO;

public interface VoteRestController {

	@SuppressWarnings("rawtypes")
	ResponseEntity vote(VoteDTO voteDTO, BindingResult result);

	@SuppressWarnings("rawtypes")
	ResponseEntity hasVote(Long userId, Date day); 
}