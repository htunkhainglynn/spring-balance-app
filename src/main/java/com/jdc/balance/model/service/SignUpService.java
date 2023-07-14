package com.jdc.balance.model.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.jdc.balance.model.domain.entity.User;
import com.jdc.balance.model.domain.form.SignUpForm;
import com.jdc.balance.model.repo.UserRepo;
import com.jdc.balance.security.SignUpException;

@Component
public class SignUpService {

	@Autowired
	private AuthenticationManager authManager;
	
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	@Autowired
	private UserRepo repo;

	@Autowired
	SecurityContextRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public String signUp(SignUpForm form, HttpServletRequest request, HttpServletResponse response) {
		var userExists = repo.findOneByLoginId(form.getLoginId());

		if (userExists.isPresent()) {
			throw new SignUpException("User already exists");
		}

		form.setPassword(passwordEncoder.encode(form.getPassword()));
		User user = new User(form);
		repo.save(user);
		
		// authenticate
		var authentication = authManager.authenticate(form.authentication());
//		System.out.println("Hello World1");
//
//		// add to security context
//		var securityContext = SecurityContextHolder.getContext();
//		securityContext.setAuthentication(authentication);
//		System.out.println("Hello World2");
//
//		// save security context holder to security context repository
//		repository.saveContext(securityContext, request, response);
//		System.out.println("Hello World3");
//
//		// redirect saved URL
//		String redirectUrl =  getSaveRequest(request, response).map(SavedRequest::getRedirectUrl)
//				.orElse("/");
//		System.out.println("Hello World");

		return "redirect:%s".formatted("/");
	}
	
	// sp3-04-52-About-Saved-Request
	private Optional<SavedRequest> getSaveRequest(HttpServletRequest request, HttpServletResponse response) {
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		return Optional.ofNullable(savedRequest);
	}

}
