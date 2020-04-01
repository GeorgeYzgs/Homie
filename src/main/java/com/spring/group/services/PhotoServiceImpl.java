package com.spring.group.services;

import com.spring.group.repos.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author George.Giazitzis
 */
public class PhotoServiceImpl implements PhotoServiceInterface {

    @Autowired
    PhotoRepository photoRepository;
}
