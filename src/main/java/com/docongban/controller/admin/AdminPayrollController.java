package com.docongban.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Account;
import com.docongban.entity.Timekeeping;
import com.docongban.payload.PayrollResponse;
import com.docongban.repository.TimekeepingRepository;
import com.docongban.service.admin.SellerService;

/**
 * Admin tính lương nhân viên
 * Created by: NPTAN (05/05/2022)
 * Version: 1.0
 */
@RestController
@RequestMapping("/admin/payroll")
public class AdminPayrollController {
	
	@Autowired
	TimekeepingRepository timekeepingRepository;
	
	@Autowired
	SellerService sellerService;
	
	/*
	 * Hiển thị trang tính lương
	 * Created by: NPTAN (05/05/2022)
	 * Version: 1.0
	 */
	@GetMapping
	public ModelAndView payrollPage() {
		ModelAndView modelAndView = new ModelAndView("admin/payroll");
		// Danh sách nhân viên + danh sách ngày làm việc:
		List<PayrollResponse> payrolls = new ArrayList<PayrollResponse>();
		
		// 1. Lấy ra tất cả id của nhân viên:
		List<Account> sellers = sellerService.getAllSellers();
		
		// 2. Duyệt từng id của nhân viên trong bằng chấm công 
		// -> lấy ra các ngày làm việc của nhân viên đó.
		// -> Dữ liệu nhân được sẽ có dạng: (thông tin nhân viên, list các ngày làm việc)
		List<Timekeeping> timekeepings = timekeepingRepository.findAll();
		
		for(Account seller : sellers) {
			List<Date> dates = new ArrayList<Date>();
			Integer totalDate = 0;
			for(Timekeeping timekeeping : timekeepings) {
				if( seller.getId() == timekeeping.getAccountId() ) {
					dates.add(timekeeping.getDate());
					totalDate++;
				}
			}
			payrolls.add(new PayrollResponse(seller, dates, totalDate));
		}
		
		modelAndView.addObject("payrolls", payrolls);
		
		return modelAndView;
	}
	
}
