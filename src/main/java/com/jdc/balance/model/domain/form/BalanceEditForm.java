package com.jdc.balance.model.domain.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jdc.balance.model.domain.entity.Balance.Type;

public class BalanceEditForm implements Serializable {
    
    private static final long serialVersionUID = 1L;
	private BalanceSummaryForm header;
    private List<BalanceItemForm> items;

    public BalanceEditForm() {
    	header = new BalanceSummaryForm();
    	items = new ArrayList<>();
    }
    
    public BalanceSummaryForm getHeader() {
		return header;
	}
    
    public BalanceEditForm type(Type type) {
		header.setType(type);
		return this;
	}

	public void setHeader(BalanceSummaryForm header) {
		this.header = header;
	}

	public List<BalanceItemForm> getItems() {
		return items;
	}

	public void setItems(List<BalanceItemForm> items) {
		this.items = items;
	}

	public void getTotal() {
    }

	public boolean showSave() {
		return !this.items.isEmpty();
	}
}