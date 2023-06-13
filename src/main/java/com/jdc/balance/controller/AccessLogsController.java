package com.jdc.balance.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jdc.balance.controller.utils.PaginationUtils;
import com.jdc.balance.model.domain.entity.UserAccessLog.Type;
import com.jdc.balance.model.service.UserAccessLogService;

@Controller
@RequestMapping("/admin/accesslogs")
public class AccessLogsController {

	@Autowired
	private UserAccessLogService userAccessLogService;

	@GetMapping
	public String search(
			@RequestParam(required = false) Type type,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size,
			ModelMap model
			) {
		
		var userList = userAccessLogService.searchAdmin(type, username, date, page, size);
		model.addAttribute("userList", userList);
		var pagination = PaginationUtils.builder("/admin/accesslogs").page(userList).build();
		model.addAttribute("pagination", pagination);
		return "access-logs";
	}
	
	@ModelAttribute("types")
	Type[] types() {
		return Type.values();
	}
}
