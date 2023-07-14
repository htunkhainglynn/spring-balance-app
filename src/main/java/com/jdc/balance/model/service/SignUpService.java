package com.jdc.balance.model.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import com.jdc.balance.model.domain.entity.User;
import com.jdc.balance.model.domain.form.SignUpForm;
import com.jdc.balance.model.repo.UserRepo;
import com.jdc.balance.security.SignUpException;

@Component
public class SignUpService {

	@Autowired
	private UserRepo repo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public void signUp(SignUpForm form) {
		var userExists = repo.findOneByLoginId(form.getLoginId());

		if (userExists.isPresent()) {
			throw new SignUpException("User already exists");
		}

		form.setPassword(passwordEncoder.encode(form.getPassword()));
		User user = new User(form);
		repo.save(user);
	}

}
