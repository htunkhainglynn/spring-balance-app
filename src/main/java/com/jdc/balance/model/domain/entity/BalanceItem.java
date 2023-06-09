package com.jdc.balance.model.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class BalanceItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "balance_item_seq")
	@SequenceGenerator(name = "balance_item_seq")
    private int id;

	@Column(nullable = false)
    private String item;

	@Column(nullable = false)
    private int unitPrice;
	
	private int quantity;
	
	@ManyToOne
	private Balance balace;

    public Balance getBalace() {
		return balace;
	}

	public void setBalace(Balance balace) {
		this.balace = balace;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}