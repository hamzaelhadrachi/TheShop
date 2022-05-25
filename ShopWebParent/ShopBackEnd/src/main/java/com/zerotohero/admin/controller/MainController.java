package com.zerotohero.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("")
    public String viewHomePage(){
        return "index";
    }

    @GetMapping("/error")
    public String viewErrorPage(){
        return "error";
    }
}
