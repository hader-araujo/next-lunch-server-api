package com.nextlunch.api.service.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.nextlunch.api.entity.User;
import com.nextlunch.api.repository.UserJpaRepository;
import com.nextlunch.api.service.UserService;
import com.nextlunch.api.service.UserServiceImpl;
import com.nextlunch.api.service.dto.UserDTO;
import com.nextlunch.api.service.exception.ReadException;
import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

public class UserServiceTest_GetAll {

	private UserJpaRepository repository;
	private UserService service;

	private String propertyId = "id";
	private String propertyName = "name";

	private final long id1 = 1;
	private final String name1 = "name 1";

	private final long id2 = 2;
	private final String name2 = "name 2";

	private final long id3 = 3;
	private final String name3 = "name 3";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		repository = mock(UserJpaRepository.class);
		service = new UserServiceImpl(repository);
	}

	@Test
	public void GetAll_GivenEntityListShouldReturnDtoList() throws ReadException {

		List<User> userList = getListOfUser();

		when(repository.findAll()).thenReturn(userList);

		List<UserDTO> userDtoListExpected = getListOfUserDTO();
		List<UserDTO> userDtoListOut = service.getAll();

		assertThat("The list should not be empty", userDtoListOut, not(empty()));
		assertThat("The list should has 3 elements", userDtoListOut, hasSize(3));
		assertThat("The return is wrong", userDtoListOut, is(userDtoListExpected));
		for (UserDTO dtoExpected : userDtoListExpected) {
			assertThat("The return is wrong", userDtoListOut,
					hasItem(hasProperty(propertyId, equalTo(dtoExpected.getId()))));
			assertThat("The return is wrong", userDtoListOut,
					hasItem(hasProperty(propertyName, equalTo(dtoExpected.getName()))));

		}
	}

	@Test
	public void GetAll_NoneDtoShouldReturnEmptyList() throws ReadException {
		when(repository.findAll()).thenReturn(Collections.emptyList());

		List<UserDTO> userList = service.getAll();

		assertThat("The list should be empty but not null", userList, both(empty()).and(notNullValue()));
	}

	@Test
	public void GetAll_GivenExceptionOnRepositoryShouldThowException() throws ReadException {
		thrown.expect(ReadException.class);
		thrown.expectMessage(ReadExceptionMessageEnum.UNEXPECTED_EXCEPTION.name());

		when(repository.findAll()).thenThrow(new RuntimeException());

		service.getAll();
	}

	private List<User> getListOfUser() {
		User user1 = new User();
		user1.setId(id1);
		user1.setName(name1);

		User user2 = new User();
		user2.setId(id2);
		user2.setName(name2);

		User user3 = new User();
		user3.setId(id3);
		user3.setName(name3);

		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
		userList.add(user3);

		return userList;
	}

	private List<UserDTO> getListOfUserDTO() {
		UserDTO userDto1 = new UserDTO();
		userDto1.setId(id1);
		userDto1.setName(name1);

		UserDTO userDto2 = new UserDTO();
		userDto2.setId(id2);
		userDto2.setName(name2);

		UserDTO userDto3 = new UserDTO();
		userDto3.setId(id3);
		userDto3.setName(name3);

		List<UserDTO> userDtoList = new ArrayList<>();
		userDtoList.add(userDto1);
		userDtoList.add(userDto2);
		userDtoList.add(userDto3);

		return userDtoList;
	}
}
