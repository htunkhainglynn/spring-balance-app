package com.jdc.balance.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jdc.balance.controller.utils.PaginationUtils;
import com.jdc.balance.model.domain.entity.Balance;
import com.jdc.balance.model.domain.entity.Balance.Type;
import com.jdc.balance.model.service.BalanceService;

@RequestMapping("user/balance")
@Controller
public class BalanceController {
	
	@Autowired
	private BalanceService balanceService;
	
	@GetMapping
	public String index() {
		return "balance-report";
	}
	
	@GetMapping("{type}")
	public String balance(
			ModelMap model, 
			@PathVariable Type type,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
			@RequestParam(required = false) String keyword,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		model.addAttribute("title", "%s Management".formatted(StringUtils.capitalize(type.name())));
		
		var result = balanceService.search(type, dateFrom, dateTo, keyword, page, size);
		model.addAttribute("list", result);
		
		Map<String, String> params = new HashMap<>();
		params.put("type", type.name());
		params.put("dateFrom", dateFrom == null ? "" : dateFrom.format(DateTimeFormatter.ofPattern(("yyyy-MM-dd"))));
		params.put("dateTo", dateTo == null ? "" :dateTo.format(DateTimeFormatter.ofPattern(("yyyy-MM-dd"))));
		params.put("keyword", keyword == null ? "" : keyword);
		
		var pagination = PaginationUtils
							.builder("user/balance/%s".formatted(type))
							.params(params)
							.page(result)
							.build();
		
		model.addAttribute("pagination", pagination);
		
		return "balance-list";
	}

}