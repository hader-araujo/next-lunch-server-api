package com.nextlunch.api.service.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.nextlunch.api.entity.Restaurant;
import com.nextlunch.api.repository.RestaurantJpaRepository;
import com.nextlunch.api.service.RestaurantService;
import com.nextlunch.api.service.RestaurantServiceImpl;
import com.nextlunch.api.service.dto.RestaurantDTO;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

public class RestaurantServiceTest_FindOne {
	private RestaurantJpaRepository repository;
	private RestaurantService service;

	private final Long id = 1L;
	private final String name = "Restaurant name";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		repository = mock(RestaurantJpaRepository.class);
		service = new RestaurantServiceImpl(repository);
	}

	@Test
	public void findOne_GivenOneEntityShouldReturnOneDto() throws ReadException {

		Restaurant entity = new Restaurant();
		entity.setId(id);
		entity.setName(name);
		when(repository.findOne(id)).thenReturn(entity);

		RestaurantDTO dto = service.findOne(id);

		assertThat("DTO should not be null", dto, notNullValue());
		assertThat("Service should return the id", dto.getId(), equalTo(id));
		assertThat("Service should return the name", dto.getName(), equalTo(name));
	}

	@Test
	public void findOne_DontExistEntityShouldThrowException() throws ReadException {
		thrown.expect(ReadException.class);
		thrown.expectMessage(ReadExceptionMessageEnum.NOT_FOUND.name());

		when(repository.findOne(id)).thenReturn(null);

		service.findOne(id);
	}

	@Test
	public void findOne_NullIdShouldThrowException() throws ReadException {
		thrown.expect(ReadException.class);
		thrown.expectMessage(ReadExceptionMessageEnum.ID_NULL_EXCEPTION.name());

		when(repository.findOne(any(Long.class))).thenThrow(new IllegalArgumentException());

		service.findOne(null);
	}

	@Test
	public void findOne_GivenExceptionOnRepositoryShouldThowException() throws ReadException {
		thrown.expect(ReadException.class);
		thrown.expectMessage(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION.name());

		when(repository.findOne(any(Long.class))).thenThrow(new RuntimeException());

		service.findOne(id);
	}
}
