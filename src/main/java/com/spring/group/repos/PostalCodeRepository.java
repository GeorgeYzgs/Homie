package com.spring.group.repos;

import com.spring.group.models.postalcode.PostalCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostalCodeRepository extends JpaRepository<PostalCode, Integer> {

}
