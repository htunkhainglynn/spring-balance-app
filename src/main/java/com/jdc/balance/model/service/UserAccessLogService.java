package com.jdc.balance.model.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jdc.balance.model.domain.entity.UserAccessLog;
import com.jdc.balance.model.domain.entity.UserAccessLog.Type;
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

	@PreAuthorize("hasAuthority('Admin')")
	public Page<UserAccessLog> searchAdmin(Type type, String username, LocalDate date, Optional<Integer> page,
			Optional<Integer> size) {
		
		var pageInfo = PageRequest.of(
				page.orElse(0), 
				size.orElse(5),
				Sort.by("time").descending());
		
		// in case no arguments
		Specification<UserAccessLog> spec = Specification.where(null);
		
		if (null != type) {
			spec = spec.and((root, query, cb)-> cb.equal(root.get("type"), type));
		}
		
		if (StringUtils.hasLength(username)) {
			spec = spec.and((root, query, cb)-> 
							cb.like(cb.lower(root.get("username")), username.toLowerCase().concat("%")));
		}
		
		if (date != null) {
			spec = spec.and((root, query, cb)-> 
							cb.greaterThanOrEqualTo(root.get("time"), date.atStartOfDay()));
		}
		
		return repo.findAll(spec, pageInfo);
	}

//	public Page<UserAccessLog> searchAll() {
//		var pageInfo = PageRequest.of(
//				0, 
//				5,
//				Sort.by("time").descending());
//		return repo.findAll(pageInfo);
//	}
}
