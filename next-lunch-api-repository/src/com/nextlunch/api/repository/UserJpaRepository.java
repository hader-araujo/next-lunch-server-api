package com.nextlunch.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextlunch.api.entity.User;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

}
