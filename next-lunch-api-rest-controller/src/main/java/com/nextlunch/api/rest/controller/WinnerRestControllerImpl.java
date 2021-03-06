package com.nextlunch.api.rest.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nextlunch.api.service.VoteService;
import com.nextlunch.api.service.dto.WinnerDTO;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping(value = "/winner")
public class WinnerRestControllerImpl implements WinnerRestController {

	private static final Logger log = LogManager.getLogger(WinnerRestControllerImpl.class.getName());

	private VoteService service;

	@Autowired
	public WinnerRestControllerImpl(VoteService service) {
		if (service == null) {
			throw new RuntimeException("Service unavailable");
		}
		this.service = service;
	}

	@Override
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/day", method = RequestMethod.GET)
	public ResponseEntity getWinnerOfDay() {
		try {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			Date day = c.getTime();

			WinnerDTO dto = service.getWinnerOfDay(day);

			return new ResponseEntity<WinnerDTO>(dto, HttpStatus.OK);

		} catch (ReadException e) {
			switch (ReadExceptionMessageEnum.valueOf(e.getMessage())) {
			case NOT_FOUND:
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			case ID_NULL_EXCEPTION:
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			case UNEXPECTED_EXCEPTION:
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			default:
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			log.error("getWinnerOfDay::Unexpected error on rest controller", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/week", method = RequestMethod.GET)
	public ResponseEntity getWinnersOfWeek() {
		try {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			Date day = c.getTime();

			log.info("11111111111111111111111111111111111111=" + day);
			
			List<WinnerDTO> dto = service.getWinnersOfWeek(day);

			return new ResponseEntity<List<WinnerDTO>>(dto, HttpStatus.OK);

		} catch (ReadException e) {
			switch (ReadExceptionMessageEnum.valueOf(e.getMessage())) {
			case NOT_FOUND:
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			case ID_NULL_EXCEPTION:
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			case UNEXPECTED_EXCEPTION:
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			default:
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			log.error("getWinnerOfWeek::Unexpected error on rest controller", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
