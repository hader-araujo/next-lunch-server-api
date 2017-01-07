package com.nextlunch.api.service;

import java.util.Date;
import java.util.List;

import com.nextlunch.api.service.dto.VoteDTO;
import com.nextlunch.api.service.exception.CreateException;
import com.nextlunch.api.service.exception.ReadException;

public interface VoteService {

	VoteDTO getWinnerOfDay(Date day) throws ReadException;

	List<VoteDTO> getWinnersOfWeek(Date day) throws ReadException;

	VoteDTO vote(Long restaurantId, Long userId, Date day) throws CreateException;
}