package com.jdc.balance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/users")
public class UserController {

    public String Search(String name) {
        return "";
    }
    
    @GetMapping
    public String users() {
    	return "users";
    }
    
    @GetMapping("delete/{id:\\d+}")
    public String delete(@PathVariable int id) {
		return "redirect:/admin/users";
    }

}