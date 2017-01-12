package com.nextlunch.api.rest.controller;

import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nextlunch.api.service.VoteService;
import com.nextlunch.api.service.dto.GetVoteDTO;
import com.nextlunch.api.service.dto.VoteDTO;
import com.nextlunch.api.service.exception.CreateException;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.CreateExceptionMessageEnum;

@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping(value = "/vote")
public class VoteRestControllerImpl implements VoteRestController {

	private static final Logger log = LogManager.getLogger(VoteRestControllerImpl.class.getName());

	private VoteService service;

	@Autowired
	public VoteRestControllerImpl(VoteService service) {
		if (service == null) {
			throw new RuntimeException("Service unavailable");
		}
		this.service = service;
	}

	@Override
	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity vote(@RequestBody @Validated VoteDTO voteDTO, BindingResult result) {
		try {
			if (result.hasErrors()) {
				return ResponseEntity.badRequest()
						.body(result.getAllErrors().stream().map(error -> error.getCodes()[0]).toArray());
			}
			VoteDTO dto = service.vote(voteDTO, Calendar.getInstance());

			return new ResponseEntity<>(dto, HttpStatus.CREATED);

		} catch (CreateException e) {
			if (e.getMessage().equals(CreateExceptionMessageEnum.VOTE_AFTER_MIDDLE_DAY_EXCEPTION.name())){
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error("vote::Unexpected error on rest controller", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ResponseEntity hasVote(@PathVariable("userId") Long userId) {
		try {
			Date day = Calendar.getInstance().getTime();

			GetVoteDTO getVoteDTO = new GetVoteDTO();
			getVoteDTO.setUserId(userId);
			getVoteDTO.setDay(day);

			boolean hasVote = service.hasVote(getVoteDTO);

			return new ResponseEntity<>(hasVote, HttpStatus.CREATED);

		} catch (ReadException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error("hasVote::Unexpected error on rest controller", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
