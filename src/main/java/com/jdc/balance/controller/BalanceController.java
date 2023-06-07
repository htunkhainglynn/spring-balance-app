package com.jdc.balance.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jdc.balance.model.domain.entity.Balance;
import com.jdc.balance.model.domain.entity.Balance.Type;;

@RequestMapping("user/balance")
@Controller
public class BalanceController {
	
	@GetMapping
	public String index() {
		return "balance-report";
	}
	
	@GetMapping("/{type}")
	public String balance(ModelMap model, @PathVariable String type) {
		model.addAttribute("title", type.equals("income") ? "Income Management" : "Expense Management");
		model.addAttribute("type", type);
		return "balance-list";
	}

	@GetMapping("add/{type}")
    public String addNew(@PathVariable String type, ModelMap model) {
		model.addAttribute("title", type.equals("income") ? "Add New Income" : "Add New Expense");
        return "balance-edit";
    }

	@GetMapping("edit/{id}")
    public String edit(@PathVariable int id, ModelMap model) {
		model.addAttribute("title", "Edit Balance");
        return "balance-edit";
    }

	@PostMapping
    public String save(Balance balance) {
        return "redirect:/";
    }

    @GetMapping("{id:\\d+}")
    public String findById(@PathVariable int id, ModelMap model) {
    	System.out.println("Balance ID is %d".formatted(id));
    	return "balance-details";
    }

    public String search(Type type, String category, LocalDate from, LocalDate to) {
        return "";
    }

}