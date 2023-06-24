package com.jdc.balance.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jdc.balance.model.domain.entity.Balance.Type;
import com.jdc.balance.model.domain.vo.LineChartVo;
import com.jdc.balance.model.service.BalanceService;

@Controller
@RequestMapping("/user/home")
public class HomeController {
	
	@Autowired
	BalanceService service;

	@GetMapping
	public String balance(
			ModelMap model, 
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo
			) {
		
		var pieResult = service.searchPie(dateFrom, dateTo);
		StringBuilder title = new StringBuilder();
		title.append("Balance Report");
		model.addAttribute("title", title);
		
		if (dateFrom != null) {
			model.addAttribute("title", title.append(" From " + dateFrom));
		}
		
		if (dateTo != null) {
			model.addAttribute("title", title.append(" To " + dateTo));
		}
		
		model.addAttribute("Incomes", pieResult.getIncome());
		model.addAttribute("Expenses", pieResult.getExpense());
		
		LineChartVo barResults = service.searchBar(dateFrom, dateTo);
		
		List<Integer> incomeList = barResults.getIncomeList();
		List<Integer> expenseList = barResults.getExpenseList();
		List<Integer> totalList = barResults.getTotalList();

		// Combine all the lists into a single stream
		List<Integer> combinedList = new ArrayList<>();
		combinedList.addAll(incomeList);
		combinedList.addAll(expenseList);
		combinedList.addAll(totalList);

		// Find the maximum value using stream and max() function
		int max = combinedList.stream()
		                      .max(Integer::compare)
		                      .orElse(Integer.MIN_VALUE);
		                      
		
		model.addAttribute("max", max);
		model.addAttribute("keySet", barResults.getLocalDateList());
		model.addAttribute("incomeValues", incomeList);
		model.addAttribute("expenseValues", expenseList);
		model.addAttribute("totalValues", totalList);
		
		return "home";
	}
	
	@ModelAttribute("balanceTypes")
	public Type[] balanceTypes() {
		return Type.values();
	}
}
