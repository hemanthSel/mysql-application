package com.mysql.mysql_application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class demo {

    @GetMapping("/")
    public String home() {
        return "index"; // Returns index.html from templates folder
    }
}
