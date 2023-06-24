package com.jdc.balance.model.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jdc.balance.model.domain.entity.Balance;
import com.jdc.balance.model.domain.entity.Balance.Type;
import com.jdc.balance.model.domain.entity.BalanceItem;
import com.jdc.balance.model.domain.form.BalanceEditForm;
import com.jdc.balance.model.domain.vo.BalanceReportVo;
import com.jdc.balance.model.domain.vo.LineChartVo;
import com.jdc.balance.model.domain.vo.PieChartVo;
import com.jdc.balance.model.repo.BalanceItemRepo;
import com.jdc.balance.model.repo.BalanceRepo;
import com.jdc.balance.model.repo.UserRepo;

@Service
public class BalanceService {

	@Autowired
	private BalanceRepo repo;

	@Autowired
	private BalanceItemRepo itemRepo;

	@Autowired
	private UserRepo userRepo;

	@PreAuthorize("authenticated()")
	public Page<BalanceItem> search(Type type, LocalDate dateFrom, LocalDate dateTo, String keyword,
			Optional<Integer> page, Optional<Integer> size) {

		var username = SecurityContextHolder.getContext().getAuthentication().getName();

		var pageInfo = PageRequest.of(page.orElse(0), size.orElse(10));

		Specification<BalanceItem> spec = (root, query, cb) -> cb.equal(root.get("balance").get("user").get("loginId"),
				username);

		if (dateFrom != null) {
			spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("balance").get("date"), dateFrom));
		}

		if (dateTo != null) {
			spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("balance").get("date"), dateFrom));
		}

		if (StringUtils.hasLength(keyword)) {
		    Specification<BalanceItem> category = spec.and((root, query, cb) 
		            -> cb.like(cb.lower(root.get("balance").get("category")), "%%%s%%".formatted(keyword).toLowerCase()));

		    Specification<BalanceItem> item = spec.and((root, query, cb) 
		            -> cb.like(cb.lower(root.get("item")), "%%%s%%".formatted(keyword).toLowerCase()));

		    spec = spec.and(category.or(item));
		}


		return itemRepo.findAll(spec, pageInfo);
	}

	public BalanceEditForm findById(Integer id) {
		return repo.findById(id).map(BalanceEditForm::new).orElseThrow();
	}

	@Transactional
	public int save(BalanceEditForm form) {

		// add new or edit
		var balance = form.getHeader().getId() == 0 ? new Balance()
				: repo.findById(form.getHeader().getId()).orElseThrow();

		var username = SecurityContextHolder.getContext().getAuthentication().getName();

		var user = userRepo.findOneByLoginId(username).orElseThrow();

		balance.setUser(user);
		balance.setCategory(form.getHeader().getCategory());
		balance.setDate(form.getHeader().getDate());
		balance.setType(form.getHeader().getType());

		balance = repo.save(balance); // merge because no item list is set

		for (var formItem : form.getItems()) {
			var item = formItem.getId() == 0 ? new BalanceItem() : itemRepo.findById(formItem.getId()).orElseThrow();

			if (formItem.isDelete()) {
				itemRepo.delete(item);
				continue; // do not go below
			}

			item.setItem(formItem.getItem());
			item.setQuantity(formItem.getQuantity());
			item.setUnitPrice(formItem.getUnitPrice());
			item.setBalance(balance); // cascade.Persist

			itemRepo.save(item);
		}

		return balance.getId();
	}

	@Transactional
	public void deleteById(int id) {
		repo.deleteById(id);
	}

	@PreAuthorize("authenticated()")
	public Page<BalanceReportVo> searchReport(Type type, LocalDate dateFrom, LocalDate dateTo, Optional<Integer> page,
			Optional<Integer> size) {
		
		var username = SecurityContextHolder.getContext().getAuthentication().getName();

		var pageInfo = PageRequest.of(page.orElse(0), size.orElse(10));
		
		Specification<Balance> spec = (root, query, cb) -> cb.equal(root.get("user").get("loginId"),
				username);
		
		if (type != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
		}
		
		if (dateFrom != null) {
			spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), dateFrom));
		}
		
		if (dateTo != null) {
			spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), dateTo));
		}
		
		var result = repo.findAll(spec, pageInfo).map(BalanceReportVo::new);
		
		// calculate net amount
		if (!result.getContent().isEmpty()) {
			var firstId = result.getContent().get(0).getId();
			
			// get balance before first search result
			var lastIncome = itemRepo.getLastBalance(firstId, Type.income).map(Number::intValue).orElse(0);
			var lastExpense = itemRepo.getLastBalance(firstId, Type.expense).map(Number::intValue).orElse(0);
			
			// calculate total of before first search result
			var totalBalance = lastIncome - lastExpense;
			
			// calculate with search results
			for (var vo : result.getContent()) {
				if (vo.getType() == Type.income) {
					totalBalance += vo.getAmount();
				} else {
					totalBalance -= vo.getAmount();
				}
				vo.setBalance(totalBalance);
			}
		}
		
		return result;
	}

	@PreAuthorize("authenticated()")
	public PieChartVo searchPie(LocalDate dateFrom, LocalDate dateTo) {
		PieChartVo pieChartVo = new PieChartVo();
		
		var username = SecurityContextHolder.getContext().getAuthentication().getName();
		Specification<Balance> spec = (root, query, cb) -> cb.equal(root.get("user").get("loginId"),
				username);
		
		var result = pieChartUtil(spec);
		pieChartVo.setTotal(result.getIncome() - result.getExpense());
		
		if (dateFrom != null) {
			spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), dateFrom));
		}
		
		if (dateTo != null) {
			spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), dateTo));
		}
		
		result = pieChartUtil(spec);
		pieChartVo.setIncome(result.getIncome());
		pieChartVo.setExpense(result.getExpense());
		
		return pieChartVo;
	}
	
	private PieChartVo pieChartUtil(Specification<Balance> spec) {
		PieChartVo pieChartVo = new PieChartVo();
		var result = repo.findAll(spec);
		
		if (!result.isEmpty()) {
			int totalIncome = 0;
			int totalExpense = 0;
			
			for (Balance balance : result) {
				totalIncome += itemRepo.getTotalBalance(balance.getId(), Type.income).map(Number::intValue).orElse(0);
				totalExpense += itemRepo.getTotalBalance(balance.getId(), Type.expense).map(Number::intValue).orElse(0);
			}
			
			pieChartVo.setIncome(totalIncome);
			pieChartVo.setExpense(totalExpense);
		}
		
		return pieChartVo;
	}

	@PreAuthorize("authenticated()")
	public LineChartVo searchBar(LocalDate dateFrom, LocalDate dateTo) {
		var username = SecurityContextHolder.getContext().getAuthentication().getName();
		Specification<Balance> spec = (root, query, cb) -> cb.equal(root.get("user").get("loginId"),
				username);
		
		if (dateFrom != null) {
			spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), dateFrom));
		}
		
		if (dateTo != null) {
			spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), dateTo));
		}
		
		Sort sort = Sort.by(Sort.Direction.ASC, "date"); // Sort in ascending order by "date" field
		
		List<Balance> balance = repo.findAll(spec, sort);
		
		List<Integer> incomeList = new ArrayList<>();
		List<Integer> expenseList = new ArrayList<>();
		List<Integer> totalList = new ArrayList<>();
		List<LocalDate> localDateList =new ArrayList<>();
		
		if (!balance.isEmpty()) {
			
			int totalIncome = 0;
			int totalExpense = 0;
			int totalBalance = 0;
			
			for (Balance b : balance) {
				totalIncome += itemRepo.getTotalBalanceByDate(b.getDate(), Type.income).map(Number::intValue).orElse(0);
				totalExpense += itemRepo.getTotalBalanceByDate(b.getDate(), Type.expense).map(Number::intValue).orElse(0);
				totalBalance = totalIncome - totalExpense;
				
				incomeList.add(totalIncome);
				expenseList.add(totalExpense);
				totalList.add(totalBalance);
				localDateList.add(b.getDate());
			}
		}
		
		return new LineChartVo(incomeList, expenseList, totalList, localDateList);
	}
	

}
