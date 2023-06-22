package com.jdc.balance.model.domain.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jdc.balance.model.domain.entity.Balance;
import com.jdc.balance.model.domain.entity.Balance.Type;

public class BalanceEditForm implements Serializable {
    
    private static final long serialVersionUID = 1L;
	private BalanceSummaryForm header;
    private List<BalanceItemForm> items;

    public BalanceEditForm() {
    	header = new BalanceSummaryForm();
    	items = new ArrayList<>();
    }
    
    public BalanceEditForm(Balance entity) {
    	header = new BalanceSummaryForm();
    	header.setId(entity.getId());
    	header.setCategory(entity.getCategory());
    	header.setDate(entity.getDate());
    	header.setType(entity.getType());
    	
    	// not to be immutable
    	items = new ArrayList<>(entity.getItems().stream().map(a -> {
    		var item = new BalanceItemForm();
    		item.setId(a.getId());
    		item.setItem(a.getItem());
    		item.setQuantity(a.getQuantity());
    		item.setUnitPrice(a.getUnitPrice());
    		return item;
    	}).toList());
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

	public int getTotal() {
		return items.stream().filter(a -> !a.isDelete())
				.reduce(0, (total, a) -> total + (a.getUnitPrice() * a.getQuantity()), Integer::sum);
    }

	public boolean showSave() {
		return !this.items.isEmpty();
	}

	// to decide where to redirect id or type
	public String getQueryParam() {
		return header.getId() == 0 ? "type=%s".formatted(header.getType()) : "id=%s".formatted(header.getId());
	}
	
	public List<BalanceItemForm> getValidItems() {
		return items.stream().filter(a -> !a.isDelete()).toList();
	}

	public int getTotalQuantity() {
		return items.stream().filter(a -> !a.isDelete())
				.reduce(0, (total, a) -> total + a.getQuantity(), Integer::sum);
	}
	
	public void clear() {
		header = new BalanceSummaryForm();
		this.items.clear();
	}
}