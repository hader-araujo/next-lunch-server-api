package com.nextlunch.api.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.nextlunch.api.service.dto.GetVoteDTO;
import com.nextlunch.api.service.dto.VoteDTO;
import com.nextlunch.api.service.dto.WinnerDTO;
import com.nextlunch.api.service.exception.CreateException;
import com.nextlunch.api.service.exception.ReadException;

public interface VoteService {

	WinnerDTO getWinnerOfDay(Date day) throws ReadException;

	List<WinnerDTO> getWinnersOfWeek(Date day) throws ReadException;

	VoteDTO vote(VoteDTO voteDTO, Calendar calendar) throws CreateException;
	
	boolean hasVote(GetVoteDTO getVoteDTO) throws ReadException;
}