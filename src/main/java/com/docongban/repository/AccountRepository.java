package com.docongban.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.docongban.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	@Query(value = "select * from account where phone =?1 and password =?2", nativeQuery = true)
	public Account findAccount(String phone, String password);
	
	@Query(value = "select * from account where phone =?1", nativeQuery = true)
	public Optional<Account> findAccountByPhone(String phone);
	
	@Query(value = "select * from account where role =?1", nativeQuery = true)
	List<Account> findByRole(String role);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE account SET role = ?1 WHERE id = ?2 ", nativeQuery = true)
	public Integer addSeller(String role , Integer accountId);

}
