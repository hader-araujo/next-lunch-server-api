package com.nextlunch.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nextlunch.api.entity.User;
import com.nextlunch.api.repository.UserJpaRepository;
import com.nextlunch.api.service.dto.UserDTO;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private static final Logger log = LogManager.getLogger(UserServiceImpl.class.getName());

	private UserJpaRepository repository;

	@Autowired
	public UserServiceImpl(UserJpaRepository repository) {
		if (repository == null) {
			throw new RuntimeException("Repository unavailable");
		}
		this.repository = repository;
	}

	@Override
	public List<UserDTO> getAll() throws ReadException {
		try {
			List<User> userList = repository.findAll();
			return userList.stream().map(f -> new UserDTO(f))
					.collect(Collectors.toList());
		} catch (Exception e) {
			log.error(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
			throw new ReadException(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION, e);
		}
	}
	
	@Override
	public UserDTO findOne(Long id) throws ReadException {
		try {
			User entity = repository.findOne(id);

			if (entity == null) {
				throw new ReadException(ReadExceptionMessageEnum.NOT_FOUND);
			}

			log.debug("findOne::Entity finded: " + entity);
			UserDTO dto = new UserDTO(entity);
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
