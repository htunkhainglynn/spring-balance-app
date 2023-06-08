package com.jdc.balance.model.domain.vo;

import com.jdc.balance.model.domain.entity.User;

public class UserVo {

    private int id;

    private String name;
    
    private String loginId;

	private boolean status;

    private String phone;
    
	private String email;
	
	public UserVo() {}
	
	public UserVo(User user) {
		this.name = user.getName();
		this.loginId = user.getLoginId();
		this.email = user.getEmail();
		this.phone = user.getPhone();
	}
	
    public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}