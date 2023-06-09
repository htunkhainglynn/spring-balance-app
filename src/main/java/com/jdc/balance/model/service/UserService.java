package com.jdc.balance.model.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.jdc.balance.model.domain.entity.User;
import com.jdc.balance.model.domain.entity.User.Role;
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

	public List<UserVo> search(Boolean status, String name, String phone) {
		Specification<User> spec = (root, query, builder) -> 
									builder.equal(root.get("role"), Role.Member); 
									
		if(status != null) {
			spec = spec.and((root, query, builder) ->
							builder.equal(root.get("active"), status)); 
		}
		
		if(StringUtils.hasLength(name)) {
			spec = spec.and((root, query, builder) ->
							builder.like(builder.lower(root.get("name")), name.toLowerCase().concat("%")));
		}
		
		if(StringUtils.hasLength(phone)) {
			spec = spec.and((root, query, builder) ->
							builder.like(root.get("phone"), phone.concat("%")));
		}
									
		// map to userVo
		return repo.findAll(spec).stream().map(UserVo::new).toList();
	}

	@Transactional
	public void changeStatus(int id, boolean status) {
		repo.findById(id).ifPresent(user -> user.setActive(status));
		
	}

	public List<UserVo> searchAll() {
		Specification<User> spec = (root, query, builder) -> 
									builder.equal(root.get("role"), Role.Member); 
		return repo.findAll(spec).stream().map(UserVo::new).toList();
	}

}
