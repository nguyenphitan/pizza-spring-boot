package com.docongban.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docongban.entity.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

}
