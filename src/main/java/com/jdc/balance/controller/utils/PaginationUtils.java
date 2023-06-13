package com.jdc.balance.controller.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PaginationUtils {

	private int current;
	private int total;
	private boolean first;
	private boolean last;
	private List<Integer> pages;
	private String url;
	
	private PaginationUtils(Builder builder) {
		this.current = builder.current;
		this.total = builder.total;
		this.first = builder.first;
		this.last = builder.last;
		this.url = builder.url;
		this.pages = new ArrayList<>();
		this.pages.add(current);
		
		// for numbers between next and previous buttons (forward)
		while(pages.size() < 5 && pages.get(0) > 0) {
			this.pages.add(0, pages.get(0) - 1);
		}
		
		// backward
		while(pages.size() < 5 && pages.get(pages.size() - 1) < total - 1) {
			this.pages.add(pages.get(pages.size() - 1) + 1);
		}
	}
	
	public int getCurrent() {
		return current;
	}
	public int getTotal() {
		return total;
	}
	public boolean isFirst() {
		return first;
	}
	public boolean isLast() {
		return last;
	}
	public List<Integer> getPages() {
		return pages;
	}
	public String getUrl() {
		return url;
	}
	public static Builder builder(String url) {
		return new Builder(url); 
	}
	
	public static class Builder {
		private int current;
		private int total;
		private boolean first;
		private boolean last;
		private String url;
		
		public Builder(String url) {
			this.url = url;
		}
		
		public <T> Builder page(Page<T> page) {
			this.current = page.getNumber();
			this.total = page.getTotalPages();
			this.first = page.isFirst();
			this.last = page.isLast();
			return this;
		}
		
		public Builder current(int current) {
			this.current = current;
			return this;
		}
		
		public Builder total(int total) {
			this.total = total;
			return this;
		}
		
		public Builder first(boolean first) {
			this.first = first;
			return this;
		}
		
		public Builder last(boolean last) {
			this.last = last;
			return this;
		}
		
		public Builder url(String url) {
			this.url = url;
			return this;
		}
		
		public PaginationUtils build() {
			return new PaginationUtils(this);
		}
	}
}
