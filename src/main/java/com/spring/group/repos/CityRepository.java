package com.spring.group.repos;

import com.spring.group.models.postalcode.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {


    /**
     * Fetches the top 10 cities that contain a substring in their name
     *
     * @param query the substring to be checked
     * @return the List<City> of the Cities
     */
    List<City> findTop10CitiesByCityNameContainingIgnoreCase(String query);
}
