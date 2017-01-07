package com.nextlunch.api.service.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.nextlunch.api.entity.Restaurant;
import com.nextlunch.api.entity.User;
import com.nextlunch.api.entity.Vote;
import com.nextlunch.api.repository.VoteJpaRepository;
import com.nextlunch.api.service.RestaurantService;
import com.nextlunch.api.service.UserService;
import com.nextlunch.api.service.VoteService;
import com.nextlunch.api.service.VoteServiceImpl;
import com.nextlunch.api.service.dto.VoteDTO;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

public class VoteServiceTest_GetWinnerOfDay {
	private VoteJpaRepository repository;
	private RestaurantService restaurantService;
	private UserService userService;
	private VoteService service;

	private Long restaurantId = 1L;
	private Long userId = 2L;
	private Date day;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		repository = mock(VoteJpaRepository.class);
		restaurantService = mock(RestaurantService.class);
		userService = mock(UserService.class);
		service = new VoteServiceImpl(repository, restaurantService, userService);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 15);
		day = cal.getTime();
	}

	@Test
	public void getWinnerOfDay_ExistWinnerShouldReturnDTO() throws ReadException {
		Restaurant restaurant = new Restaurant();
		restaurant.setId(restaurantId);
		User user = new User();
		user.setId(userId);
		Vote vote = new Vote();
		vote.setRestaurant(restaurant);
		vote.setUser(user);
		vote.setDay(day);
		
		when(repository.getWinnerOfDay(day)).thenReturn(vote);

		VoteDTO dto = service.getWinnerOfDay(day);

		verify(repository, times(1)).getWinnerOfDay(day);
		assertThat("DTO should not be null", dto, notNullValue());
		assertThat("Service should return the restaurant", dto.getRestaurantId(), equalTo(restaurantId));
		assertThat("Service should return the user", dto.getUserId(), equalTo(userId));
		assertThat("Service should return the day", dto.getDay(), equalTo(day));
	}

	@Test
	public void getWinnerOfDay_DontExistWinnerShouldReturnNull() throws ReadException {
		when(repository.getWinnerOfDay(day)).thenReturn(null);

		VoteDTO dto = service.getWinnerOfDay(day);

		verify(repository, times(1)).getWinnerOfDay(day);
		assertThat("DTO should not be null", dto, nullValue());
	}

	@Test
	public void getWinnerOfDay_NullDayShouldThrowException() throws ReadException {
		thrown.expect(ReadException.class);
		thrown.expectMessage(ReadExceptionMessageEnum.DATE_NULL_EXCEPTION.name());

		service.getWinnerOfDay(null);
	}

	@Test
	public void getWinnerOfDay_GivenExceptionOnRepositoryShouldThowException() throws ReadException {
		thrown.expect(ReadException.class);
		thrown.expectMessage(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION.name());

		when(repository.getWinnerOfDay(any(Date.class))).thenThrow(new RuntimeException());

		service.getWinnerOfDay(day);
	}
}
