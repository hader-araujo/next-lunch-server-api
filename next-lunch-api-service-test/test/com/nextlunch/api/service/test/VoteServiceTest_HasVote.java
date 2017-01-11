package com.nextlunch.api.service.test;

import static org.junit.Assert.assertTrue;
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
import com.nextlunch.api.service.dto.GetVoteDTO;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

public class VoteServiceTest_HasVote {

	private VoteJpaRepository repository;
	private RestaurantService restaurantService;
	private UserService userService;
	private VoteService service;

	private Long restaurantId = 1L;
	private Long userId = 2L;


	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		repository = mock(VoteJpaRepository.class);
		restaurantService = mock(RestaurantService.class);
		userService = mock(UserService.class);
		service = new VoteServiceImpl(repository, restaurantService, userService);
	}

	@Test
	public void vote_GivenEntityShouldVerifyIfItHasVote() throws ReadException, ReadException {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 11);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date day = c.getTime();

		GetVoteDTO getVoteDTO = new GetVoteDTO();
		getVoteDTO.setDay(day);
		getVoteDTO.setUserId(userId);

		Vote vote = new Vote();

		Restaurant restaurant = new Restaurant();
		restaurant.setId(restaurantId);
		User user = new User();
		user.setId(userId);
		vote.setRestaurant(restaurant);
		vote.setUser(user);
		vote.setDay(day);

		when(repository.findByDayAndUser_Id(day, userId)).thenReturn(vote);

		boolean hasVote = service.hasVote(getVoteDTO);

		verify(repository, times(1)).findByDayAndUser_Id(day, userId);

		assertTrue("Return show is true", hasVote);
	}

	@Test
	public void vote_GivenExceptionOnRepositoryShouldThowException() throws ReadException {
		thrown.expect(ReadException.class);
		thrown.expectMessage(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION.name());

		when(repository.findByDayAndUser_Id(any(Date.class), any(Long.class))).thenThrow(new RuntimeException());

		GetVoteDTO getVoteDTO = new GetVoteDTO();
		getVoteDTO.setUserId(userId);
		service.hasVote(getVoteDTO);
	}

}
