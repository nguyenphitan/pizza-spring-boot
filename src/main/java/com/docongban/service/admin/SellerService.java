package com.docongban.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.docongban.entity.Account;

@Service
public interface SellerService {
	// Lấy tất cả các account có role là SELLER
	List<Account> getAllSellers();
	
	// Thêm Seller:
	int addSeller(int id);
	
	// Xóa Seller:
	void deleteSeller(int id);
	
}
