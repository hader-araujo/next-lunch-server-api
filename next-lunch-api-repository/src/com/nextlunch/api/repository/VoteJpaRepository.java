package com.nextlunch.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextlunch.api.entity.Vote;

@Repository
public interface VoteJpaRepository extends JpaRepository<Vote, Long> {
	
	List<Vote> findByDay(Date day);
	
	Vote findByDayAndUser_Id(Date day, Long userId);
}
