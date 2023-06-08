package com.jdc.balance.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jdc.balance.model.domain.entity.User;
import com.jdc.balance.model.domain.form.SignUpForm;
import com.jdc.balance.model.repo.UserRepo;

@Component
public class SignUpService {
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void signUp(SignUpForm form) {
		form.setPassword(passwordEncoder.encode(form.getPassword()));
		User user = new User(form);
		repo.save(user);
	}

}
