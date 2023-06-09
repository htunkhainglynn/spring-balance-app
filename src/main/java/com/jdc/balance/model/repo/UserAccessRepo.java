package com.jdc.balance.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jdc.balance.model.domain.entity.AccessLog;

public interface UserAccessRepo extends JpaRepository<AccessLog, Integer> {

}
