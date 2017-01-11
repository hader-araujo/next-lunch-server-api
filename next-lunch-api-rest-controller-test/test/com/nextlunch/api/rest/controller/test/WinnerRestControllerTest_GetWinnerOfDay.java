package com.nextlunch.api.rest.controller.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nextlunch.api.rest.controller.WinnerRestController;
import com.nextlunch.api.rest.controller.WinnerRestControllerImpl;
import com.nextlunch.api.service.VoteService;
import com.nextlunch.api.service.dto.WinnerDTO;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

public class WinnerRestControllerTest_GetWinnerOfDay {
	private VoteService service;
	private WinnerRestController controller;
	private Date day = Calendar.getInstance().getTime();
	private Long restaurantId = 1L;

	@Before
	public void setup() {
		service = mock(VoteService.class);
		controller = new WinnerRestControllerImpl(service);
	}

	@Test
	public void get_GivenIdShouldReturnOKStatusWithData() throws ReadException {
		WinnerDTO dtoToReturn = new WinnerDTO();
		dtoToReturn.setQuantity(1L);
		dtoToReturn.setRestaurantId(restaurantId);

		when(service.getWinnerOfDay(any(Date.class))).thenReturn(dtoToReturn);

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.getWinnerOfDay();

		HttpStatus httpStatus = responseEntity.getStatusCode();
		WinnerDTO body = (WinnerDTO) responseEntity.getBody();

		assertThat("Wrong HTTP status for correct ID", httpStatus, equalTo(HttpStatus.OK));
		assertThat("Missing error code", body, hasProperty("quantity", (equalTo(1L))));
		assertThat("Missing error code", body, hasProperty("restaurantId", (equalTo(restaurantId))));

		verify(service, times(1)).getWinnerOfDay(any(Date.class));
	}

	@Test
	public void get_GivenUnknownIdShouldReturnNotFoundStatus() throws ReadException {
		when(service.getWinnerOfDay(any(Date.class))).thenThrow(new ReadException(ReadExceptionMessageEnum.NOT_FOUND));

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.getWinnerOfDay();

		HttpStatus httpStatus = responseEntity.getStatusCode();

		assertThat("Wrong HTTP status for unknown ID", httpStatus, equalTo(HttpStatus.NOT_FOUND));

		verify(service, times(1)).getWinnerOfDay(any(Date.class));
	}

	@Test
	public void get_GivenNullIdShouldReturnBadRequestStatus() throws ReadException {
		when(service.getWinnerOfDay(any(Date.class))).thenThrow(new ReadException(ReadExceptionMessageEnum.ID_NULL_EXCEPTION));

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.getWinnerOfDay();

		HttpStatus httpStatus = responseEntity.getStatusCode();
		assertThat("Wrong HTTP status for null ID", httpStatus, equalTo(HttpStatus.BAD_REQUEST));
		verify(service, times(1)).getWinnerOfDay(any(Date.class));
	}

	@Test
	public void get_GivenExceptionOnServiceShouldReturnInternalServerErrorStatus() throws ReadException {
		when(service.getWinnerOfDay(any(Date.class)))
				.thenThrow(new ReadException(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION));

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.getWinnerOfDay();

		HttpStatus httpStatus = responseEntity.getStatusCode();
		assertThat("Wrong HTTP status", httpStatus, equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
		verify(service, times(1)).getWinnerOfDay(any(Date.class));
	}

	@Test
	public void get_GivenAnyExceptionShoudReturnInternalServerErrorStatus() throws ReadException {
		doThrow(new RuntimeException()).when(service).getWinnerOfDay(any(Date.class));

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.getWinnerOfDay();

		HttpStatus httpStatus = responseEntity.getStatusCode();
		assertThat("Wrong HTTP status", httpStatus, equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
		verify(service, times(1)).getWinnerOfDay(any(Date.class));
	}
}
