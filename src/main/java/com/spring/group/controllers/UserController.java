package com.spring.group.controllers;

import com.spring.group.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author George.Giazitzis
 */
@Controller
public class UserController {

    @Autowired
    UserServiceImpl userServiceImpl;


    @GetMapping(path = "")
    public String testToBeDeleted() {
        return "fragments";
    }
}
