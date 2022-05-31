package com.docongban.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.docongban.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	
	@Modifying
	@Query(value = "delete FROM order_detail where order_account_id=?1", nativeQuery = true)
	void deleteByOrderAccountId(int orderAccountId);
}
