package com.nextlunch.api.service.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

public class VoteServiceTest_GetWinnersOfWeek {
	private VoteJpaRepository repository;
	private RestaurantService restaurantService;
	private UserService userService;
	private VoteService service;

	private String propertyDay = "day";

	private Long restaurantId = 1L;
	private Long userId = 2L;
	private Date day;
	private Date day15;
	private Date day16;
	private Date day17;
	private Date day18;
	private Date day19;
	private Date day20;
	private Date day21;

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
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		day = cal.getTime();
	}

	@Test
	public void getWinnersOfWeek_ExistWinnerShouldReturnDTO() throws ReadException {
		setDays();

		Restaurant restaurant = new Restaurant();
		restaurant.setId(restaurantId);
		User user = new User();
		user.setId(userId);
		Vote vote = new Vote();
		vote.setRestaurant(restaurant);
		vote.setUser(user);
		vote.setDay(day15);
		when(repository.getWinnerOfDay(day15)).thenReturn(vote);

		vote = new Vote();
		vote.setRestaurant(restaurant);
		vote.setUser(user);
		vote.setDay(day19);
		when(repository.getWinnerOfDay(day19)).thenReturn(vote);

		when(repository.getWinnerOfDay(day16)).thenReturn(null);
		when(repository.getWinnerOfDay(day17)).thenReturn(null);
		when(repository.getWinnerOfDay(day18)).thenReturn(null);
		when(repository.getWinnerOfDay(day20)).thenReturn(null);
		when(repository.getWinnerOfDay(day21)).thenReturn(null);

		List<VoteDTO> dtoList = service.getWinnersOfWeek(day);

		verify(repository, times(7)).getWinnerOfDay(any(Date.class));
		assertThat("The list should has 2 elements", dtoList, hasSize(2));

		assertThat("The return is wrong", dtoList, hasItem(hasProperty(propertyDay, equalTo(day15))));
		assertThat("The return is wrong", dtoList, hasItem(hasProperty(propertyDay, equalTo(day19))));
	}

	@Test
	public void getWinnersOfWeek_DontExistWinnerShouldReturnNull() throws ReadException {
		setDays();
		when(repository.getWinnerOfDay(day15)).thenReturn(null);
		when(repository.getWinnerOfDay(day16)).thenReturn(null);
		when(repository.getWinnerOfDay(day17)).thenReturn(null);
		when(repository.getWinnerOfDay(day18)).thenReturn(null);
		when(repository.getWinnerOfDay(day19)).thenReturn(null);
		when(repository.getWinnerOfDay(day20)).thenReturn(null);
		when(repository.getWinnerOfDay(day21)).thenReturn(null);

		List<VoteDTO> dtoList = service.getWinnersOfWeek(day);

		verify(repository, times(7)).getWinnerOfDay(any(Date.class));
		assertThat("DTO list should be empty", dtoList, hasSize(0));
	}

	@Test
	public void getWinnersOfWeek_NullDayShouldThrowException() throws ReadException {
		thrown.expect(ReadException.class);
		thrown.expectMessage(ReadExceptionMessageEnum.DATE_NULL_EXCEPTION.name());

		service.getWinnersOfWeek(null);
	}

	@Test
	public void getWinnersOfWeek_GivenExceptionOnRepositoryShouldThowException() throws ReadException {
		thrown.expect(ReadException.class);
		thrown.expectMessage(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION.name());

		when(repository.getWinnerOfDay(any(Date.class))).thenThrow(new RuntimeException());

		service.getWinnersOfWeek(day);
	}

	private void setDays() {
		Calendar cal = Calendar.getInstance();

		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 15);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		day15 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 16);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		day16 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 17);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		day17 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 18);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		day18 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 19);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		day19 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 20);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		day20 = cal.getTime();

		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 21);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		day21 = cal.getTime();
	}
}
