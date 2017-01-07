package com.nextlunch.api.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nextlunch.api.entity.Restaurant;
import com.nextlunch.api.entity.User;
import com.nextlunch.api.entity.Vote;
import com.nextlunch.api.repository.VoteJpaRepository;
import com.nextlunch.api.service.dto.RestaurantDTO;
import com.nextlunch.api.service.dto.UserDTO;
import com.nextlunch.api.service.dto.VoteDTO;
import com.nextlunch.api.service.exception.CreateException;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.CreateExceptionMessageEnum;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

@Service
@Transactional(readOnly = true)
public class VoteServiceImpl implements VoteService {

	private static final Logger log = LogManager.getLogger(VoteServiceImpl.class.getName());

	private VoteJpaRepository repository;
	private RestaurantService restaurantService;
	private UserService userService;

	@Autowired
	public VoteServiceImpl(VoteJpaRepository repository, RestaurantService restaurantService, UserService userService) {

		if (repository == null) {
			throw new RuntimeException("Repository unavailable");
		}
		if (restaurantService == null) {
			throw new RuntimeException("Repository unavailable");
		}
		if (userService == null) {
			throw new RuntimeException("Repository unavailable");
		}

		this.repository = repository;
		this.restaurantService = restaurantService;
		this.userService = userService;
	}

	@Override
	public VoteDTO getWinnerOfDay(Date day) throws ReadException {
		try {

			if (day == null) {
				throw new ReadException(ReadExceptionMessageEnum.DATE_NULL_EXCEPTION);
			}

			Vote vote = repository.getWinnerOfDay(day);

			return vote == null ? null : new VoteDTO(vote);
		} catch (Exception e) {
			if (e instanceof ReadException
					&& e.getMessage().equals(ReadExceptionMessageEnum.DATE_NULL_EXCEPTION.name())) {
				throw e;
			}
			log.error(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
			throw new ReadException(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
		}
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
			c.add(Calendar.DAY_OF_MONTH, --dayOfWeek);
			dateList.add(c.getTime());
		}

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

		return dateList;
	}

	@Override
	public List<VoteDTO> getWinnersOfWeek(Date day) throws ReadException {
		try {
			if (day == null) {
				throw new ReadException(ReadExceptionMessageEnum.DATE_NULL_EXCEPTION);
			}
			List<Date> days = getDaysOfWeek(day);

			List<VoteDTO> winners = new ArrayList<>();

			for (Date dayOfWeek : days) {
				if (dayOfWeek != null) {
					VoteDTO v = getWinnerOfDay(dayOfWeek);
					if (v != null) {
						winners.add(v);
					}
				}
			}

			return winners;
		} catch (ReadException e) {
			throw e;
		} catch (Exception e) {
			log.error(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
			throw new ReadException(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public VoteDTO vote(Long restaurantId, Long userId, Date day) throws CreateException {
		try {
			
			Calendar c = Calendar.getInstance();
			c.setTime(day);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			day = c.getTime();
			
			if (isDateExists(restaurantId, day)) {
				throw new CreateException(CreateExceptionMessageEnum.VOTE_SAME_WEEK_EXCEPTION);
			}

			RestaurantDTO restaurantDTO = restaurantService.findOne(restaurantId);
			UserDTO userDTO = userService.findOne(userId);

			Vote vote = new Vote();
			vote.setRestaurant(getRestaurant(restaurantDTO));
			vote.setUser(getUser(userDTO));
			vote.setDay(day);

			Vote voteAdded = repository.saveAndFlush(vote);
			return new VoteDTO(voteAdded);
		} catch (CreateException e) {
			throw e;
		} catch (Exception e) {
			log.error(CreateExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
			throw new CreateException(CreateExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
		}
	}

	private boolean isDateExists(Long restaurantId, Date day) throws ReadException {
		List<VoteDTO> winners = getWinnersOfWeek(day);
		
		return winners.stream().filter(p -> p.getRestaurantId().equals(restaurantId)).findAny().isPresent();
	}

	private Restaurant getRestaurant(RestaurantDTO restaurantDTO) {
		Restaurant restaurant = new Restaurant();
		restaurant.setId(restaurantDTO.getId());
		restaurant.setName(restaurantDTO.getName());
		return restaurant;
	}

	private User getUser(UserDTO userDTO) {
		User user = new User();
		user.setId(userDTO.getId());
		user.setName(userDTO.getName());
		return user;
	}
}
