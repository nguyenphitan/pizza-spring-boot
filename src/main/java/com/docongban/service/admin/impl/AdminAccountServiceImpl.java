package com.docongban.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docongban.entity.Account;
import com.docongban.repository.AccountRepository;
import com.docongban.service.admin.AdminAccountService;

@Component
public class AdminAccountServiceImpl implements AdminAccountService{

	@Autowired
	AccountRepository accountRepository;
	
	@Override
	public List<Account> getAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		return accounts;
	}

}
