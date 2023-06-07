package com.jdc.balance.model.domain.form;

import javax.validation.constraints.NotBlank;

public class SignUpForm {

	@NotBlank(message = "Name cannot be empty.")
    private String name;

	@NotBlank(message = "Username cannot be empty.")
    private String loginId;

	@NotBlank(message = "Password cannot be empty.")
	private String password;
	
    @Override
	public String toString() {
		return "SignUpForm [name=" + name + ", loginId=" + loginId + ", password=" + password + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}