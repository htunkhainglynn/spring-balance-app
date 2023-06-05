package com.jdc.balance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/home")
public class HomeController {

	@GetMapping("/incomes")
	public String incomes(ModelMap model) {
		model.addAttribute("title", "Income Management");
		return "balance-list";
	}
	
	@GetMapping("/expenses")
	public String expenses(ModelMap model) {
		model.addAttribute("title", "Expense Management");
		return "balance-list";
	}
	
	@GetMapping("/users")
	public String users() {
		return "users";
	}
}
