package com.jdc.balance.model.domain.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.jdc.balance.model.domain.form.SignUpForm;

@Entity
public class User implements Serializable {

	public User() {}
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "user_seq")
	@SequenceGenerator(name = "user_seq")
	private int id;

	@Column(nullable = false)
    private String name;

	// to be case sensitive
	@Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8_bin")
	private String loginId;

	@Column(nullable = false)
    private String password;

    private String phone;

    private String email;

    private boolean active;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
	public User(SignUpForm form) {
		this.name = form.getName();
		this.loginId = form.getLoginId();
		this.password = form.getPassword();
		this.role = Role.Member;
		this.active = true;
	}


    public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@OneToMany(mappedBy = "user")
    private List<Balance> balance;
    
    public List<Balance> getBalance() {
		return balance;
	}

	public void setBalance(List<Balance> balance) {
		this.balance = balance;
	}

	public enum Role {
        Admin,
        Member
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}