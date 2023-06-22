package com.jdc.balance.model.domain.vo;

import java.time.LocalDate;

import com.jdc.balance.model.domain.entity.Balance;
import com.jdc.balance.model.domain.entity.Balance.Type;

public class BalanceReportVo {

	private int id;
	
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private LocalDate date;

    private Type type;

    private String category;

    private int amount;
    
    private int balance;
    
    public BalanceReportVo() {}
    
    public BalanceReportVo(Balance entity) {
    	this.id = entity.getId();
    	this.date = entity.getDate();
    	this.type = entity.getType();
    	this.category = entity.getCategory();
    	this.amount = entity.getItems().stream().mapToInt(a -> a.getQuantity() * a.getUnitPrice()).sum();
    }
    
    public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getIncome() {
		return type == Type.income ? amount : 0;
	}
	
	public int getExpense() {
		return type == Type.expense ? amount : 0;
	}
}