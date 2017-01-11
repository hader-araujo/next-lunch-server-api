package com.nextlunch.api.rest.controller.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.nextlunch.api.rest.controller.VoteRestController;
import com.nextlunch.api.rest.controller.VoteRestControllerImpl;
import com.nextlunch.api.service.VoteService;
import com.nextlunch.api.service.dto.VoteDTO;
import com.nextlunch.api.service.exception.CreateException;
import com.nextlunch.api.service.exception.enums.CreateExceptionMessageEnum;

public class VoteRestControllerTest_Vote {
	private VoteService service;
	private VoteRestController controller;
	private BindingResult result;

	private String fieldName_Name = "name";

	private Long restaurantId = 1L;
	private Long userId = 2L;

	@Before
	public void setup() {
		result = mock(BindingResult.class);
		service = mock(VoteService.class);
		controller = new VoteRestControllerImpl(service);
	}

	@Test
	public void vote_GivenDTOShoudReturnOKStatus() throws CreateException {

		VoteDTO dto = new VoteDTO();
		dto.setRestaurantId(restaurantId);
		;
		dto.setUserId(userId);

		when(result.hasErrors()).thenReturn(false);

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.vote(dto, result);

		HttpStatus httpStatus = responseEntity.getStatusCode();
		assertThat("Wrong HTTP status", httpStatus, equalTo(HttpStatus.CREATED));

		verify(service, times(1)).vote(dto);
	}

	@Test
	public void vote_GivenSomeErrorsShouldReturnTheCodeForEachField() throws CreateException {
		String errorCodeNameSize = "Size.voteDTO.name";

		List<ObjectError> listObjectError = new ArrayList<>();
		listObjectError.add(new ObjectError(fieldName_Name, new String[] { errorCodeNameSize }, null, null));

		when(result.hasErrors()).thenReturn(true);
		when(result.getAllErrors()).thenReturn(listObjectError);

		@SuppressWarnings({ "rawtypes" })
		ResponseEntity responseEntity = controller.vote(null, result);

		HttpStatus httpStatus = responseEntity.getStatusCode();
		Object[] body = (Object[]) responseEntity.getBody();

		assertThat("Wrong HTTP status", httpStatus, equalTo(HttpStatus.BAD_REQUEST));
		assertThat("Missing error code", Arrays.asList(body), hasItem(equalTo(errorCodeNameSize)));

		verify(service, times(0)).vote(any(VoteDTO.class));
	}

	@Test
	public void vote_GivenExceptionOnServiceShoudReturnInternalServerErrorStatus() throws CreateException {
		doThrow(new CreateException(CreateExceptionMessageEnum.UNEXPECTED_EXCEPTION)).when(service)
				.vote(any(VoteDTO.class));
		when(result.hasErrors()).thenReturn(false);

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.vote(null, result);

		HttpStatus httpStatus = responseEntity.getStatusCode();
		assertThat("Wrong HTTP status", httpStatus, equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
		verify(service, times(1)).vote(any(VoteDTO.class));
	}

	@Test
	public void vote_GivenAnyExceptionShoudReturnInternalServerErrorStatus() throws CreateException {
		doThrow(new RuntimeException()).when(result).hasErrors();

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.vote(null, result);

		HttpStatus httpStatus = responseEntity.getStatusCode();
		assertThat("Wrong HTTP status", httpStatus, equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
		verify(service, times(0)).vote(any(VoteDTO.class));
	}
}
