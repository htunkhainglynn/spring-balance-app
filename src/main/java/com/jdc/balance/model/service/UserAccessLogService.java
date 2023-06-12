package com.jdc.balance.model.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jdc.balance.model.domain.entity.UserAccessLog;
import com.jdc.balance.model.repo.UserAccessRepo;

@Service
public class UserAccessLogService {
	
	@Autowired
	private UserAccessRepo repo;

	public Page<UserAccessLog> search(String username, Optional<Integer> page, Optional<Integer> size) {
		var pageInfo = PageRequest.of(
				page.orElse(0), 
				size.orElse(5),
				Sort.by("time").descending());
		
		Specification<UserAccessLog> spec = (root, query, builder) 
											->  builder.equal(root.get("username"), username);

		return repo.findAll(spec, pageInfo);
	}

}
