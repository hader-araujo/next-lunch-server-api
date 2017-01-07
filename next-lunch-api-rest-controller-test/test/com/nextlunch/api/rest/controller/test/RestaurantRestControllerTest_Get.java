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

import com.nextlunch.api.rest.controller.RestaurantRestController;
import com.nextlunch.api.rest.controller.RestaurantRestControllerImpl;
import com.nextlunch.api.service.RestaurantService;
import com.nextlunch.api.service.dto.RestaurantDTO;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

public class RestaurantRestControllerTest_Get {

	private RestaurantService service;
	private RestaurantRestController controller;
	private Long id1 = 1L;
	private String name1 = "My Restaurant";
	private Long id2 = 2L;
	private String name2 = "Other restaurant";

	@Before
	public void setup() {
		service = mock(RestaurantService.class);
		controller = new RestaurantRestControllerImpl(service);
	}

	@Test
	public void getAll_ShouldReturnOKStatusWithData() throws ReadException {
		List<RestaurantDTO> listDtoToReturn = new ArrayList<>();

		RestaurantDTO dtoToReturn = new RestaurantDTO();
		dtoToReturn.setId(id1);
		dtoToReturn.setName(name1);
		listDtoToReturn.add(dtoToReturn);
		dtoToReturn = new RestaurantDTO();
		dtoToReturn.setId(id2);
		dtoToReturn.setName(name2);
		listDtoToReturn.add(dtoToReturn);

		when(service.getAll()).thenReturn(listDtoToReturn);

		@SuppressWarnings("rawtypes")
		ResponseEntity responseEntity = controller.getAll();

		HttpStatus httpStatus = responseEntity.getStatusCode();
		@SuppressWarnings("unchecked")
		List<RestaurantDTO> body = (List<RestaurantDTO>) responseEntity.getBody();

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
