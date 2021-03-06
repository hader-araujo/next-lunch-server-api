package com.nextlunch.api.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nextlunch.api.entity.Restaurant;
import com.nextlunch.api.entity.User;
import com.nextlunch.api.entity.Vote;
import com.nextlunch.api.repository.VoteJpaRepository;
import com.nextlunch.api.service.dto.GetVoteDTO;
import com.nextlunch.api.service.dto.RestaurantDTO;
import com.nextlunch.api.service.dto.UserDTO;
import com.nextlunch.api.service.dto.VoteDTO;
import com.nextlunch.api.service.dto.WinnerDTO;
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

	private boolean isBeforeMiddleDay(Calendar calendar) {
		Calendar middleDay = Calendar.getInstance();
		middleDay.set(Calendar.HOUR_OF_DAY, 12);
		middleDay.set(Calendar.MINUTE, 0);
		middleDay.set(Calendar.SECOND, 0);
		middleDay.set(Calendar.MILLISECOND, 0);
		
		return calendar.before(middleDay);
	}

	@Override
	public WinnerDTO getWinnerOfDay(Date day) throws ReadException {
		try {

			if (day == null) {
				throw new ReadException(ReadExceptionMessageEnum.DATE_NULL_EXCEPTION);
			}

			List<Vote> voteList = repository.findByDay(day);

			if (voteList == null) {
				return null;
			}

			Map<Restaurant, Long> sortedMap = new LinkedHashMap<>();

			// TODO chage it putting this in database search
			voteList.stream().collect(Collectors.groupingByConcurrent(Vote::getRestaurant, Collectors.counting()))
					.entrySet().stream().sorted(Map.Entry.<Restaurant, Long> comparingByValue().reversed())
					.forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));

			List<WinnerDTO> winnerList = sortedMap.entrySet().stream()
					.filter(p -> p.getValue() == sortedMap.values().iterator().next())
					.map(f -> new WinnerDTO(f.getKey().getId(), f.getKey().getName(), f.getValue(),
							isBeforeMiddleDay(Calendar.getInstance())))
					.sorted(Comparator.comparing(WinnerDTO::getRestaurantName)).collect(Collectors.toList());

			return winnerList.isEmpty() ? null : winnerList.get(0);
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
		int delta = c.get(Calendar.DAY_OF_WEEK);
		c.add(Calendar.DAY_OF_MONTH, - delta );
		 
		List<Date> dateList = new ArrayList<>();
		while (dateList.size() < delta - 1) {
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.DAY_OF_MONTH, 1);
			dateList.add(c.getTime());
		}

		return dateList;
	}

	@Override
	public List<WinnerDTO> getWinnersOfWeek(Date day) throws ReadException {
		try {
			if (day == null) {
				throw new ReadException(ReadExceptionMessageEnum.DATE_NULL_EXCEPTION);
			}
			List<Date> days = getDaysOfWeek(day);

			List<WinnerDTO> winners = new ArrayList<>();

			for (Date dayOfWeek : days) {
				WinnerDTO v = getWinnerOfDay(dayOfWeek);
				if (v != null) {
					winners.add(v);
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
	public VoteDTO vote(VoteDTO voteDTO, Calendar calendar) throws CreateException {
		try {
			
			if (! isBeforeMiddleDay(calendar)){
				throw new CreateException(CreateExceptionMessageEnum.VOTE_AFTER_MIDDLE_DAY_EXCEPTION);
			}
			
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			Date day = c.getTime();

			if (isDateExists(voteDTO.getRestaurantId(), day)) {
				throw new CreateException(CreateExceptionMessageEnum.VOTE_SAME_WEEK_EXCEPTION);
			}

			RestaurantDTO restaurantDTO = restaurantService.findOne(voteDTO.getRestaurantId());
			UserDTO userDTO = userService.findOne(voteDTO.getUserId());

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

	@Override
	public boolean hasVote(GetVoteDTO getVoteDTO) throws ReadException {
		try {
			Vote voteAdded = repository.findByDayAndUser_Id(getVoteDTO.getDay(), getVoteDTO.getUserId());
			return voteAdded != null;
		} catch (Exception e) {
			log.error(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
			throw new ReadException(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
		}
	}

	private boolean isDateExists(Long restaurantId, Date day) throws ReadException {
		List<WinnerDTO> winners = getWinnersOfWeek(day);

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
