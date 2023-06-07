package com.jdc.balance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class SecurityController {

	@PostMapping("signin")
    public String signIn() {
        return "redirect:/";
    }

	@PostMapping("signup")
    public String signUp() {
        return "redirect:/";
    }
	
	@GetMapping("signin")
	public void loadSignIn() {}
	
	@GetMapping("signup")
	public void loadSignUp() {}

    @GetMapping("signout")
    public String signOut() {
        return "redirect:/signin";
    }
    
    @PostMapping("user/changepass")
    public String changePassword() {
        return "redirect:/";
    }

}