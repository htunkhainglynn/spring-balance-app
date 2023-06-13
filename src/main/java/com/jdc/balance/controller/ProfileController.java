package com.jdc.balance.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jdc.balance.controller.utils.PaginationUtils;
import com.jdc.balance.model.service.UserAccessLogService;
import com.jdc.balance.model.service.UserService;

@Controller
@RequestMapping("/user/profile")
public class ProfileController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAccessLogService userAccessLogService;

	@GetMapping
	public String profile(ModelMap model, 
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {
		// get user name
		var username = SecurityContextHolder.getContext().getAuthentication().getName();
		var userVo = userService.find(username);
		model.addAttribute("user", userVo);
		
		var accessLog = userAccessLogService.search(username, page, size);
		model.addAttribute("list", accessLog.getContent());
		
		// using builder pattern
		var pagination = PaginationUtils.builder("/user/profile").page(accessLog).build();
							
		
		model.addAttribute("pagination", pagination);
		
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
