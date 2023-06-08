package com.jdc.balance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jdc.balance.model.service.UserService;

@Controller
@RequestMapping("/user/profile")
public class ProfileController {
	
	@Autowired
	private UserService userService;

	@GetMapping
	public String profile(ModelMap model) {
		// get user name
		var username = SecurityContextHolder.getContext().getAuthentication().getName();
		var userVo = userService.find(username);
		model.addAttribute("user", userVo);
		return "profile";
	}
	
	@PostMapping("/contact")
	public String editContact(@RequestParam(required = false) String phone, 
							  @RequestParam(required = false) String email) {
		var username = SecurityContextHolder.getContext().getAuthentication().getName();
		userService.updateContact(username, phone, email);
		return "redirect:/user/profile";
	}
}
