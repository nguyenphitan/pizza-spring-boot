package com.docongban.service.admin;

import java.util.List;

import com.docongban.entity.Discount;

public interface DiscountService {
	// Lấy tất cả discount:
	List<Discount> getAlls();
	
	// Lấy discount theo id:
	Discount getById(Long id);
	
	// Thêm mới discount:
	Discount createDiscount(Double discount, Double value);
	
	// Sửa theo id:
	Discount update(Long id, Discount discount);
	
	// Xóa theo id:
	void delete(Long id);
	
}
