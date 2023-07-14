package com.jdc.balance.security;

public class SignUpException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public SignUpException(String message) {
		super(message);
	}
}
