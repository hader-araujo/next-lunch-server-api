package com.nextlunch.api.rest.controller.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nextlunch.api.rest.controller.VoteRestController;
import com.nextlunch.api.rest.controller.VoteRestControllerImpl;
import com.nextlunch.api.service.VoteService;
import com.nextlunch.api.service.dto.GetVoteDTO;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

public class VoteRestControllerTest_HasVote {
	private VoteService service;
	private VoteRestController controller;

	private Long userId = 2L;
	private Date day;

	@Before
	public void setup() {
		service = mock(VoteService.class);
		controller = new VoteRestControllerImpl(service);
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 11);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		day = c.getTime();
	}

	@Test
	public void vote_GivenDTOShoudReturnOKStatus() throws ReadException {

		GetVoteDTO dto = new GetVoteDTO();
		dto.setUserId(userId);
		dto.setDay(day);
		
		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.hasVote(userId, day);

		HttpStatus httpStatus = responseEntity.getStatusCode();
		assertThat("Wrong HTTP status", httpStatus, equalTo(HttpStatus.CREATED));

		verify(service, times(1)).hasVote(dto);
	}

	@Test
	public void vote_GivenExceptionOnServiceShoudReturnInternalServerErrorStatus() throws ReadException {
		doThrow(new ReadException(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION)).when(service)
		.hasVote(any(GetVoteDTO.class));
		doThrow(new RuntimeException()).when(service).hasVote(any(GetVoteDTO.class));

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.hasVote(null, null);

		HttpStatus httpStatus = responseEntity.getStatusCode();
		assertThat("Wrong HTTP status", httpStatus, equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
		verify(service, times(1)).hasVote(any(GetVoteDTO.class));
	}

}
