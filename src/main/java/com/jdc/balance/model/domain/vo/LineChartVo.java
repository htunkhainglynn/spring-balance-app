package com.jdc.balance.model.domain.vo;

import java.time.LocalDate;
import java.util.List;

public class LineChartVo {

	List<Integer> incomeList;
	List<Integer> expenseList;
	List<Integer> totalList;
	List<LocalDate> localDateList;
	
	
	public LineChartVo(List<Integer> incomeList, List<Integer> expenseList, List<Integer> totalList,
			List<LocalDate> localDateList) {
		this.incomeList = incomeList;
		this.expenseList = expenseList;
		this.totalList = totalList;
		this.localDateList = localDateList;
	}
	public List<Integer> getIncomeList() {
		return incomeList;
	}
	public void setIncomeList(List<Integer> incomeList) {
		this.incomeList = incomeList;
	}
	public List<Integer> getExpenseList() {
		return expenseList;
	}
	public void setExpenseList(List<Integer> expenseList) {
		this.expenseList = expenseList;
	}
	public List<Integer> getTotalList() {
		return totalList;
	}
	public void setTotalList(List<Integer> totalList) {
		this.totalList = totalList;
	}
	public List<LocalDate> getLocalDateList() {
		return localDateList;
	}
	public void setLocalDateList(List<LocalDate> localDateList) {
		this.localDateList = localDateList;
	}
}

