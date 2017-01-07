package com.nextlunch.api.rest.controller.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nextlunch.api.rest.controller.UserRestController;
import com.nextlunch.api.rest.controller.UserRestControllerImpl;
import com.nextlunch.api.service.UserService;
import com.nextlunch.api.service.dto.UserDTO;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

public class UserRestControllerTest_Get {

	private UserService service;
	private UserRestController controller;
	private Long id1 = 1L;
	private String name1 = "First user";
	private Long id2 = 2L;
	private String name2 = "Other user";

	@Before
	public void setup() {
		service = mock(UserService.class);
		controller = new UserRestControllerImpl(service);
	}

	@Test
	public void getAll_ShouldReturnOKStatusWithData() throws ReadException {
		List<UserDTO> listDtoToReturn = new ArrayList<>();

		UserDTO dtoToReturn = new UserDTO();
		dtoToReturn.setId(id1);
		dtoToReturn.setName(name1);
		listDtoToReturn.add(dtoToReturn);
		dtoToReturn = new UserDTO();
		dtoToReturn.setId(id2);
		dtoToReturn.setName(name2);
		listDtoToReturn.add(dtoToReturn);

		when(service.getAll()).thenReturn(listDtoToReturn);

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.getAll();

		HttpStatus httpStatus = responseEntity.getStatusCode();
		@SuppressWarnings("unchecked")
		List<UserDTO> body = (List<UserDTO>) responseEntity.getBody();

		assertThat("Wrong HTTP status", httpStatus, equalTo(HttpStatus.OK));
		assertThat("Missing entity id", body.get(0), hasProperty("id", (equalTo(id1))));
		assertThat("Missing entity name", body.get(0), hasProperty("name", (equalTo(name1))));
		assertThat("Missing entity id", body.get(1), hasProperty("id", (equalTo(id2))));
		assertThat("Missing entity name", body.get(1), hasProperty("name", (equalTo(name2))));

		verify(service, times(1)).getAll();
	}

	@Test
	public void getAll_GivenExceptionOnServiceShouldReturnInternalServerErrorStatus() throws ReadException {
		when(service.getAll()).thenThrow(new ReadException(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION));

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.getAll();

		HttpStatus httpStatus = responseEntity.getStatusCode();
		assertThat("Wrong HTTP status", httpStatus, equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
		verify(service, times(1)).getAll();
	}

	@Test
	public void getAll_GivenAnyExceptionShoudReturnInternalServerErrorStatus() throws ReadException {
		doThrow(new RuntimeException()).when(service).getAll();

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.getAll();

		HttpStatus httpStatus = responseEntity.getStatusCode();
		assertThat("Wrong HTTP status", httpStatus, equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
		verify(service, times(1)).getAll();
	}
}
