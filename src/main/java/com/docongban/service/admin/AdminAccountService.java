package com.docongban.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docongban.entity.Account;

@Service
public interface AdminAccountService {
	// Lấy danh sách tất cả người dùng:
	List<Account> getAllAccounts();
}
