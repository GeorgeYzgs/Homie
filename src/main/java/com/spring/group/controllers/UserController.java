package com.spring.group.controllers;

import com.spring.group.models.property.Category;
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


    @GetMapping("/fragment")
    public String testToBeDeleted() {
        Category.values();
        return "fragments.html";
    }

    @GetMapping("/")
    public String getHomePage() {
        return "basic-search.html";
    }

    @GetMapping("/search")
    public String getSearchPage() {
        return "full-search.html";
    }
}



