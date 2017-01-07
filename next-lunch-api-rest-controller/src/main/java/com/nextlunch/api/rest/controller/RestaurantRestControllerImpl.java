package com.nextlunch.api.rest.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nextlunch.api.service.RestaurantService;
import com.nextlunch.api.service.dto.RestaurantDTO;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

@RestController
@RequestMapping(value = "/restaurant")
public class RestaurantRestControllerImpl implements RestaurantRestController {

	private static final Logger log = LogManager.getLogger(RestaurantRestControllerImpl.class.getName());

	private RestaurantService service;

	@Autowired
	public RestaurantRestControllerImpl(RestaurantService service) {
		if (service == null) {
			throw new RuntimeException("Service unavailable");
		}
		this.service = service;
	}

	@Override
	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity getAll() {
		try {
			List<RestaurantDTO> dto = service.getAll();

			return new ResponseEntity<List<RestaurantDTO>>(dto, HttpStatus.OK);

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
			log.error("getAll::Unexpected error on rest controller", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
