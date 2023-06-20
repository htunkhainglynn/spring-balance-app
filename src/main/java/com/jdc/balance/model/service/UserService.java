package com.jdc.balance.model.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.jdc.balance.model.domain.entity.User;
import com.jdc.balance.model.domain.entity.User.Role;
import com.jdc.balance.model.domain.form.ChangePasswordForm;
import com.jdc.balance.model.domain.vo.UserVo;
import com.jdc.balance.model.repo.UserRepo;
import com.jdc.balance.security.BalanceAppException;


@Controller
public class UserService {

	@Autowired
	private UserRepo repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

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

	public List<UserVo> search(Boolean status, String name, String phone) {
		Specification<User> spec = (root, query, builder) -> builder.equal(root.get("role"), Role.Member);

		if (status != null) {
			spec = spec.and((root, query, builder) -> builder.equal(root.get("active"), status));
		}

		if (StringUtils.hasLength(name)) {
			spec = spec.and((root, query, builder) -> builder.like(builder.lower(root.get("name")),
					name.toLowerCase().concat("%")));
		}

		if (StringUtils.hasLength(phone)) {
			spec = spec.and((root, query, builder) -> builder.like(root.get("phone"), phone.concat("%")));
		}

		// map to userVo
		return repo.findAll(spec).stream().map(UserVo::new).toList();
	}

	@Transactional
	public void changeStatus(int id, boolean status) {
		repo.findById(id).ifPresent(user -> user.setActive(status));

	}

	public List<UserVo> searchAll() {
		Specification<User> spec = (root, query, builder) -> builder.equal(root.get("role"), Role.Member);
		return repo.findAll(spec).stream().map(UserVo::new).toList();
	}

	@Transactional
	public void changePassword(ChangePasswordForm form) {

		if (!StringUtils.hasLength(form.getOldPassword())) {
			throw new BalanceAppException("Old Password cannot be empty");
		}

		if (!StringUtils.hasLength(form.getNewPassword())) {
			throw new BalanceAppException("New Password cannot be empty");
		}

		if (form.getNewPassword().equals(form.getOldPassword())) {
			throw new BalanceAppException("Passwords cannot be the same");
		}
		
		var user = repo.findOneByLoginId(form.getLoginId()).orElseThrow();
		
		if (!passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
			throw new BalanceAppException("Wrong Old Password");
		}
		
		user.setPassword(passwordEncoder.encode(form.getNewPassword()));
		
		repo.save(user);  // this is no need
	}

}
