package com.nextlunch.api.service.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
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
import com.nextlunch.api.service.dto.RestaurantDTO;
import com.nextlunch.api.service.dto.UserDTO;
import com.nextlunch.api.service.dto.VoteDTO;
import com.nextlunch.api.service.exception.CreateException;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.CreateExceptionMessageEnum;

public class VoteServiceTest_Vote {

	private VoteJpaRepository repository;
	private RestaurantService restaurantService;
	private UserService userService;
	private VoteService service;

	private Long restaurantId = 1L;
	private Long userId = 2L;
	private Date day;

	private String propertyRestaurant = "restaurantId";
	private String propertyUser = "userId";
	private String propertyDay = "day";

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
	public void vote_Given2VoteTheSameWeekAndSameWinnerRestaurantShouldNotSave() throws CreateException, ReadException {
		thrown.expect(CreateException.class);
		thrown.expectMessage(CreateExceptionMessageEnum.VOTE_SAME_WEEK_EXCEPTION.name());

		Date day16 = getDay(16);
		Restaurant restaurant = new Restaurant();
		restaurant.setId(restaurantId);
		User user = new User();
		user.setId(userId);
		Vote vote = new Vote();
		vote.setRestaurant(restaurant);
		vote.setUser(user);
		vote.setDay(day16);
		when(repository.getWinnerOfDay(day16)).thenReturn(vote);

		Date day19 = getDay(19);
		vote = new Vote();
		vote.setRestaurant(restaurant);
		vote.setUser(user);
		vote.setDay(day19);
		when(repository.getWinnerOfDay(day19)).thenReturn(vote);

		when(repository.getWinnerOfDay(getDay(15))).thenReturn(null);
		when(repository.getWinnerOfDay(getDay(17))).thenReturn(null);
		when(repository.getWinnerOfDay(getDay(18))).thenReturn(null);
		when(repository.getWinnerOfDay(getDay(20))).thenReturn(null);
		when(repository.getWinnerOfDay(getDay(21))).thenReturn(null);

		service.vote(restaurantId, userId, day);
	}

	@Test
	public void vote_GivenEntityShouldSave() throws CreateException, ReadException {
		Vote vote = new Vote();

		Restaurant restaurant = new Restaurant();
		restaurant.setId(restaurantId);
		User user = new User();
		user.setId(userId);
		vote.setRestaurant(restaurant);
		vote.setUser(user);
		vote.setDay(day);

		RestaurantDTO restaurantDTO = new RestaurantDTO();
		restaurantDTO.setId(restaurantId);

		UserDTO userDTO = new UserDTO();
		userDTO.setId(userId);

		when(repository.saveAndFlush(vote)).thenReturn(vote);
		when(restaurantService.findOne(restaurantId)).thenReturn(restaurantDTO);
		when(userService.findOne(userId)).thenReturn(userDTO);

		when(repository.getWinnerOfDay(getDay(15))).thenReturn(null);
		when(repository.getWinnerOfDay(getDay(16))).thenReturn(null);
		when(repository.getWinnerOfDay(getDay(17))).thenReturn(null);
		when(repository.getWinnerOfDay(getDay(18))).thenReturn(null);
		when(repository.getWinnerOfDay(getDay(19))).thenReturn(null);
		when(repository.getWinnerOfDay(getDay(20))).thenReturn(null);
		when(repository.getWinnerOfDay(getDay(21))).thenReturn(null);

		VoteDTO dto = service.vote(restaurantId, userId, day);

		verify(repository, times(1)).saveAndFlush(vote);

		assertThat("DTO should has restaurant", dto, hasProperty(propertyRestaurant, equalTo(restaurantId)));
		assertThat("DTO should has user", dto, hasProperty(propertyUser, equalTo(userId)));
		assertThat("DTO should has date", dto, hasProperty(propertyDay, equalTo(day)));
	}

	@Test
	public void vote_GivenExceptionOnRepositoryShouldThowException() throws CreateException {
		thrown.expect(CreateException.class);
		thrown.expectMessage(CreateExceptionMessageEnum.UNEXPECTED_EXCEPTION.name());

		when(repository.findAll()).thenThrow(new RuntimeException());

		service.vote(restaurantId, userId, day);
	}

	private Date getDay(int day) {
		Calendar cal = Calendar.getInstance();

		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();

	}

}
