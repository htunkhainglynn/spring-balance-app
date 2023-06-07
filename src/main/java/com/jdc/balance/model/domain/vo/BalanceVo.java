package com.jdc.balance.model.domain.vo;

import java.util.List;

import com.jdc.balance.model.domain.form.BalanceItemForm;

public class BalanceVo {

    private BalanceReportVo header;
    
    private List<BalanceItemForm> items;

	public BalanceReportVo getHeader() {
		return header;
	}

	public void setHeader(BalanceReportVo header) {
		this.header = header;
	}

	public List<BalanceItemForm> getItems() {
		return items;
	}

	public void setItems(List<BalanceItemForm> items) {
		this.items = items;
	}
}