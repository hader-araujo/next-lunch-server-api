package com.nextlunch.api.service.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class RestaurantServiceTest_GetAll {

	private RestaurantJpaRepository repository;
	private RestaurantService service;

	private String propertyId = "id";
	private String propertyName = "name";
	
	private final long id1 = 1;
	private final String name1 = "name 1";

	private final long id2 = 2;
	private final String name2 = "name 2";

	private final long id3 = 3;
	private final String name3 = "name 3";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		repository = mock(RestaurantJpaRepository.class);
		service = new RestaurantServiceImpl(repository);
	}

	@Test
	public void GetAll_GivenEntityListShouldReturnDtoList() throws ReadException {

		List<Restaurant> restaurantList = getListOfRestaurant();

		when(repository.findAll()).thenReturn(restaurantList);

		List<RestaurantDTO> restaurantDtoListExpected = getListOfRestaurantDTO();
		List<RestaurantDTO> restaurantDtoListOut = service.getAll();

		assertThat("The list should not be empty", restaurantDtoListOut, not(empty()));
		assertThat("The list should has 3 elements", restaurantDtoListOut, hasSize(3));
		assertThat("The return is wrong", restaurantDtoListOut, is(restaurantDtoListExpected));
		for (RestaurantDTO dtoExpected : restaurantDtoListExpected){
			assertThat("The return is wrong", restaurantDtoListOut, hasItem( hasProperty(propertyId, equalTo(dtoExpected.getId()))));
			assertThat("The return is wrong", restaurantDtoListOut, hasItem(hasProperty(propertyName, equalTo(dtoExpected.getName()))));
			
		}
	}

	@Test
	public void GetAll_NoneDtoShouldReturnEmptyList() throws ReadException {
		when(repository.findAll()).thenReturn(Collections.emptyList());

		List<RestaurantDTO> restaurantList = service.getAll();

		assertThat("The list should be empty but not null", restaurantList, both(empty()).and(notNullValue()));
	}

	@Test
	public void GetAll_GivenExceptionOnRepositoryShouldThowException() throws ReadException {
		thrown.expect(ReadException.class);
		thrown.expectMessage(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION.name());

		when(repository.findAll()).thenThrow(new RuntimeException());

		service.getAll();
	}

	private List<Restaurant> getListOfRestaurant() {
		Restaurant restaurant1 = new Restaurant();
		restaurant1.setId(id1);
		restaurant1.setName(name1);

		Restaurant restaurant2 = new Restaurant();
		restaurant2.setId(id2);
		restaurant2.setName(name2);

		Restaurant restaurant3 = new Restaurant();
		restaurant3.setId(id3);
		restaurant3.setName(name3);

		List<Restaurant> restaurantList = new ArrayList<>();
		restaurantList.add(restaurant1);
		restaurantList.add(restaurant2);
		restaurantList.add(restaurant3);

		return restaurantList;
	}

	private List<RestaurantDTO> getListOfRestaurantDTO() {
		RestaurantDTO restaurantDto1 = new RestaurantDTO();
		restaurantDto1.setId(id1);
		restaurantDto1.setName(name1);

		RestaurantDTO restaurantDto2 = new RestaurantDTO();
		restaurantDto2.setId(id2);
		restaurantDto2.setName(name2);

		RestaurantDTO restaurantDto3 = new RestaurantDTO();
		restaurantDto3.setId(id3);
		restaurantDto3.setName(name3);

		List<RestaurantDTO> restaurantDtoList = new ArrayList<>();
		restaurantDtoList.add(restaurantDto1);
		restaurantDtoList.add(restaurantDto2);
		restaurantDtoList.add(restaurantDto3);

		return restaurantDtoList;
	}
}
