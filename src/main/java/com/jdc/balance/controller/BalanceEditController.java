package com.jdc.balance.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.jdc.balance.model.domain.entity.Balance.Type;
import com.jdc.balance.model.domain.form.BalanceEditForm;
import com.jdc.balance.model.domain.form.BalanceItemForm;
import com.jdc.balance.model.domain.form.BalanceSummaryForm;
import com.jdc.balance.model.service.BalanceService;
import com.jdc.balance.security.BalanceAppException;

@Component
@RequestMapping("user/balance-edit")
@SessionAttributes("balanceEditForm")
public class BalanceEditController {

	@Autowired
	BalanceService service;

	@GetMapping
	public String edit(@ModelAttribute("balanceEditForm") BalanceEditForm form,
			@RequestParam(required = false) Integer id, @RequestParam(required = false) Type type) {

		// income and expense change session
		if (null != id && form.getHeader().getId() != id) {
			var result = service.fetchForm(id);
			form.setHeader(result.getHeader());
			form.setItems(result.getItems());
		}

		// income and expense change session
		if (type != null && !form.getHeader().getType().equals(type)) {
			form.setHeader(new BalanceSummaryForm());
			form.getHeader().setType(type);
			form.getItems().clear();
		}
		return "balance-edit";
	}

	@GetMapping("confirm")
	public String confirm() {
		return "balance-edit-confirm";
	}

	@PostMapping("item")
	public String addItem(@ModelAttribute("balanceEditForm") BalanceEditForm form,
			@Valid @ModelAttribute("itemForm") BalanceItemForm itemForm, BindingResult result) {

		if (result.hasErrors()) {
			return "balance-edit";
		}

		form.getItems().add(itemForm);

		// to decide where to redirect id or type
		var queryParam = form.getHeader().getId() == 0 ? "type=%s".formatted(form.getHeader().getType())
				: "id=%s".formatted(form.getHeader().getId());

		return "redirect:/user/balance-edit?%s".formatted(queryParam);
	}

	@PostMapping
	public String save() {
		return "redirect:/user/balance/%d".formatted(1);
	}

	@GetMapping("item/delete")
	public String itemDelete(@ModelAttribute("balanceEditForm") BalanceEditForm form,
			@RequestParam int id) {
		
		var itemList = form.getItems();
		itemList.remove(id);
		
		var queryParam = form.getHeader().getId() == 0 ? "type=%s".formatted(form.getHeader().getType())
				: "id=%s".formatted(form.getHeader().getId());

		return "redirect:/user/balance-edit?%s".formatted(queryParam);
	}

	// create session
	@ModelAttribute("balanceEditForm")
	public BalanceEditForm form(@RequestParam(required = false) Integer id, @RequestParam(required = false) Type type) {

		if (id != null) {
			return service.fetchForm(id);
		}

		if (type == null) {
			throw new BalanceAppException("Please set type for form");
		}

		return new BalanceEditForm().type(type);
	}

	@ModelAttribute("itemForm")
	public BalanceItemForm itemForm() {
		return new BalanceItemForm();
	}
}
