package com.spring.group.services;

import com.spring.group.repos.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author George.Giazitzis
 */
@Service
@Transactional
public class PhotoServiceImpl implements PhotoServiceInterface {

    @Autowired
    private PhotoRepository photoRepository;
}
