package com.nextlunch.api.service.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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

		boolean[] first = new boolean[] { true };

		getDaysOfWeek(day).forEach(c -> {
			if (first[0]) {
				Restaurant restaurant = new Restaurant();
				restaurant.setId(restaurantId);
				User user = new User();
				user.setId(userId);
				Vote vote = new Vote();
				vote.setRestaurant(restaurant);
				vote.setUser(user);
				vote.setDay(c);
				when(repository.findByDay(c)).thenReturn(Arrays.asList(vote));
				first[0] = false;
			} else {
				when(repository.findByDay(c)).thenReturn(null);
			}
		});

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		VoteDTO voteDTO = new VoteDTO();
		voteDTO.setRestaurantId(restaurantId);
		voteDTO.setUserId(userId);
		service.vote(voteDTO, cal);
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

		getDaysOfWeek(day).forEach(c -> when(repository.findByDay(c)).thenReturn(null));

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		VoteDTO voteDTO = new VoteDTO();
		voteDTO.setRestaurantId(restaurantId);
		voteDTO.setUserId(userId);
		VoteDTO dto = service.vote(voteDTO, cal);

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

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		VoteDTO voteDTO = new VoteDTO();
		voteDTO.setRestaurantId(restaurantId);
		voteDTO.setUserId(userId);
		service.vote(voteDTO, cal);
	}

	private List<Date> getDaysOfWeek(Date day) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		List<Date> dateList = new ArrayList<>();
		while (dayOfWeek > 1) {
			c = Calendar.getInstance();
			c.setTime(day);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.DAY_OF_MONTH, (--dayOfWeek) * -1);
			dateList.add(c.getTime());
		}

		if (dateList.size() == 6) {
			c = Calendar.getInstance();
			c.setTime(day);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			dateList.add(c.getTime());
		} else {
			for (int i = dateList.size(); i < 7; i++) {
				c = Calendar.getInstance();
				c.setTime(day);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				c.add(Calendar.DAY_OF_MONTH, i);
				dateList.add(c.getTime());
			}
		}

		return dateList;
	}
}
