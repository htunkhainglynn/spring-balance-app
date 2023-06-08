package com.jdc.balance.model.service;

import java.util.Optional;
import java.util.function.Consumer;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.jdc.balance.model.domain.entity.User;
import com.jdc.balance.model.domain.vo.UserVo;
import com.jdc.balance.model.repo.UserRepo;

@Controller
public class UserService {
	
	@Autowired
	private UserRepo repo;

	public UserVo find(String username) {
		Optional<User> user = repo.findOneByLoginId(username);
		return user.map(UserVo::new).orElseThrow();
	}

	@Transactional
	public void updateContact(String username, String phone, String email) {
		Consumer<User> userConsumer = user -> {
			user.setEmail(email);
			user.setPhone(phone);
		};
		
		// if present do this function
		repo.findOneByLoginId(username).ifPresent(userConsumer);
	}

}
