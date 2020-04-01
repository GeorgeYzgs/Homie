package com.spring.group.controllers;

import com.spring.group.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author George.Giazitzis
 */
@Controller
public class UserController {

    @Autowired
    UserServiceImpl userServiceImpl;

}
