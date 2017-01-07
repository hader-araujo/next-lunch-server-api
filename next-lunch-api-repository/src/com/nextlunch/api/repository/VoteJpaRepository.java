package com.nextlunch.api.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nextlunch.api.entity.Vote;

@Repository
public interface VoteJpaRepository extends JpaRepository<Vote, Long> {
	
	@Query("select top 1 from vote where day = ?1 group by restaurant_id order by count(*) desc")
	Vote getWinnerOfDay(Date day);
}
