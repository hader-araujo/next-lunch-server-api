package com.nextlunch.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextlunch.api.entity.Restaurant;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<Restaurant, Long> {

}
