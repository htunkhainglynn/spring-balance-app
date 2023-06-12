package com.jdc.balance.model.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name ="access_log")
public class UserAccessLog implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "access_log_seq")
	@SequenceGenerator(name = "access_log_seq")
	private int id;
	
	private String username;
	
	private LocalDateTime time;
	
	private String errorMessage;

	@Enumerated(EnumType.STRING)
	private Type type;
	
	public UserAccessLog() {}
	
	public UserAccessLog(String username, Type type, LocalDateTime time) {
		this.username = username;
		this.type = type;
		this.time = time;
	}
	
	// in case there is error
	public UserAccessLog(String username, Type type, LocalDateTime time, String errorMessage) {
		this.username = username;
		this.type = type;
		this.time = time;
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public enum Type {
		SIGNIN, SIGNOUT, ERROR
	}

}
