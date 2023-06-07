package com.jdc.balance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class UserController {

    public String Search(String name) {
        return "";
    }
    
    @GetMapping("users")
    public String users() {
    	return "users";
    }
    
    @GetMapping("user/delete/{id:\\d+}")
    public String delete() {
    	return "redirect:/users";
    }

}