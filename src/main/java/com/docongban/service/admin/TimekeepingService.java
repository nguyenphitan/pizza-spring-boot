package com.docongban.service.admin;

import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Timekeeping;

public interface TimekeepingService {
	// Lấy danh sách nhân viên:
	ModelAndView getAlls();
	
	// Lưu chấm công:
	Timekeeping save(Timekeeping timekeeping);
	
	// Xóa chấm công:
	void delete(Integer accountId);
}
