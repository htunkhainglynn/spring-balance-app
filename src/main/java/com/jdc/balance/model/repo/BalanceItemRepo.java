package com.jdc.balance.model.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.jdc.balance.model.domain.entity.Balance.Type;
import com.jdc.balance.model.domain.entity.BalanceItem;

public interface BalanceItemRepo extends JpaRepository<BalanceItem, Integer>, JpaSpecificationExecutor<BalanceItem> {

	@Query("SELECT SUM(bi.unitPrice * bi.quantity) FROM BalanceItem bi WHERE bi.balance.id < :id AND bi.balance.type = :type")
	Optional<Number> getLastBalance(int id, Type type);

}
