package com.spring.group.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String getHomePage() {
        return "basic-search.html";
    }

    @GetMapping("/search")
    public String getSearchPage() {
        return "full-search.html";
    }
}
