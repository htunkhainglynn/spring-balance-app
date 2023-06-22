package com.jdc.balance.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
			var result = service.findById(id);
			form.setHeader(result.getHeader());
			form.setItems(result.getItems());
		}

		// income and expense change session
		if (type != null && form.getHeader().getType() != type) {
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
			@ModelAttribute("itemForm") @Valid BalanceItemForm itemForm, BindingResult result) {

		if (result.hasErrors()) {
			return "balance-edit";
		}

		form.getItems().add(itemForm);

		return "redirect:/user/balance-edit?%s".formatted(form.getQueryParam());
	}

	@PostMapping
	public String save(@ModelAttribute("balanceEditForm") BalanceEditForm form,
			@ModelAttribute("summaryForm") @Valid BalanceSummaryForm summaryForm, BindingResult result) {

		if (result.hasErrors()) {
			return "balance-edit-confirm";
		}

		form.getHeader().setCategory(summaryForm.getCategory());
		form.getHeader().setDate(summaryForm.getDate());

		var id = service.save(form);
		form.clear();
		return "redirect:/user/balance/details/%d".formatted(id);
	}

	@GetMapping("item/delete")
	public String itemDelete(@ModelAttribute("balanceEditForm") BalanceEditForm form, @RequestParam int id) {

		var item = form.getItems().get(id);

		if (item.getId() == 0) { // it is not from database
			form.getItems().remove(item);
		} else {
			item.setDelete(true);
		}

		return "redirect:/user/balance-edit?%s".formatted(form.getQueryParam());
	}

	// create session
	@ModelAttribute("balanceEditForm")
	public BalanceEditForm form(@RequestParam(required = false) Integer id, @RequestParam(required = false) Type type) {

		if (id != null) {
			return service.findById(id);
		}

		if (type == null) {
			throw new BalanceAppException("Please set type for form");
		}

		// set type to balanceHeaderForm
		return new BalanceEditForm().type(type);
	}

	@ModelAttribute("itemForm")
	public BalanceItemForm itemForm() {
		return new BalanceItemForm();
	}

	@ModelAttribute("summaryForm")  // video no 61 6:23
	public BalanceSummaryForm summaryForm(@ModelAttribute("balanceEditForm") BalanceEditForm form) {
		return form.getHeader();
	}
}
