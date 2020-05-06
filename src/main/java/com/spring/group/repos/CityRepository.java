package com.spring.group.repos;

import com.spring.group.models.postalcode.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {

    List<City> findTop10CitiesByCityNameContainingIgnoreCase(String query);
}
