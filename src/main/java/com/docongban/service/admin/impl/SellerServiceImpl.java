package com.docongban.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docongban.entity.Account;
import com.docongban.repository.AccountRepository;
import com.docongban.service.admin.SellerService;

@Component
public class SellerServiceImpl implements SellerService {
	
	@Autowired
	AccountRepository accountRepository;

	@Override
	public List<Account> getAllSellers() {
		List<Account> accounts = accountRepository.findByRole("SELLER");
		return accounts;
	}

	@Override
	public int addSeller(int id) {
		Integer result = accountRepository.addSeller("SELLER", id);
		return result;
	}

	@Override
	public void deleteSeller(int id) {
		Integer result = accountRepository.addSeller("USER", id);
	}

	
}
