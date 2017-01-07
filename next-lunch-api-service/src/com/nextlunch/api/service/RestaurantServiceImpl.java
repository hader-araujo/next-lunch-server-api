package com.nextlunch.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nextlunch.api.entity.Restaurant;
import com.nextlunch.api.repository.RestaurantJpaRepository;
import com.nextlunch.api.service.dto.RestaurantDTO;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

@Service
@Transactional(readOnly = true)
public class RestaurantServiceImpl implements RestaurantService {

	private static final Logger log = LogManager.getLogger(RestaurantServiceImpl.class.getName());

	private RestaurantJpaRepository repository;

	@Autowired
	public RestaurantServiceImpl(RestaurantJpaRepository repository) {
		if (repository == null) {
			throw new RuntimeException("Repository unavailable");
		}
		this.repository = repository;
	}

	@Override
	public List<RestaurantDTO> getAll() throws ReadException {
		try {
			List<Restaurant> restaurantList = repository.findAll();
			return restaurantList.stream().map(f -> new RestaurantDTO(f))
					.collect(Collectors.toList());
		} catch (Exception e) {
			log.error(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
			throw new ReadException(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
		}
	}
	
	@Override
	public RestaurantDTO findOne(Long id) throws ReadException {
		try {
			Restaurant entity = repository.findOne(id);

			if (entity == null) {
				throw new ReadException(ReadExceptionMessageEnum.NOT_FOUND);
			}

			log.debug("findOne::Entity finded: " + entity);
			RestaurantDTO dto = new RestaurantDTO(entity);
			return dto;

		} catch (ReadException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			throw new ReadException(ReadExceptionMessageEnum.ID_NULL_EXCEPTION);
		} catch (Exception e) {
			log.error(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
			throw new ReadException(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
		}
	}

}
