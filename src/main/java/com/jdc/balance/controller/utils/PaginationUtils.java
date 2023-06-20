package com.jdc.balance.controller.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

public class PaginationUtils {

	private int current;
	private int total;
	private boolean first;
	private boolean last;
	private List<Integer> pages;
	private String url;
	private Map<String, String> params;
	private List<Integer> sizes;
	private String sizeChangeFormId;

	private PaginationUtils(Builder builder) {
		this.current = builder.current;
		this.total = builder.total;
		this.first = builder.first;
		this.last = builder.last;
		this.url = builder.url;
		this.params = builder.params;
		this.sizeChangeFormId = builder.sizeChangeFormId;
		
		// same with params
		this.sizes = null == builder.sizes ? new ArrayList<>() : builder.sizes;
		this.pages = new ArrayList<>();
		this.pages.add(current);
		
		if (null == this.params) {
			params = new HashMap<>();
		}

		// for numbers between next and previous buttons (to get selected one middle)
		while (pages.size() < 3 && pages.get(0) > 0) {
			this.pages.add(0, pages.get(0) - 1);
		}

		// backward
		while (pages.size() < 5 && pages.get(pages.size() - 1) < total - 1) {
			this.pages.add(pages.get(pages.size() - 1) + 1);
		}
		
		// for numbers between next and previous buttons (forward)[for end elements]
		while (pages.size() < 5 && pages.get(0) > 0) {
			this.pages.add(0, pages.get(0) - 1);
		}
	}
	
	public boolean isShow() {
		return pages.size() > 1;
	}
	
	public String getSizeChangeFormId() {
		return sizeChangeFormId;
	}
	
	public String getParams() {
		return params.entrySet().stream().map(a -> "%s=%s".formatted(a.getKey(), a.getValue()))
									.reduce("",(a, b) -> "%s&%s".formatted(a, b));
	}
	
	public List<Integer> getSizes() {
		return sizes;
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
		private Map<String, String> params;
		private List<Integer> sizes;
		private String sizeChangeFormId;

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
		
		public Builder params(Map<String, String> params) {
			this.params = params;
			return this;
		}
		
		public Builder sizes(List<Integer> sizes) {
			this.sizes = sizes;
			return this;
		}

		public Builder sizeChangeFormId(String sizeChangeFormId) {
			this.sizeChangeFormId = sizeChangeFormId;
			return this;
		}
		public PaginationUtils build() {
			return new PaginationUtils(this);
		}
	}
}
