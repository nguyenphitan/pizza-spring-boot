package com.docongban.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.payload.PayrollResponse;
import com.docongban.service.admin.PayrollService;

/**
 * Admin tính lương nhân viên
 * Created by: NPTAN (05/05/2022)
 * Version: 1.0
 */
@RestController
@RequestMapping("/admin/payroll")
public class AdminPayrollController {
	@Autowired
	private PayrollService payrollService;
	
	/*
	 * Hiển thị trang tính lương
	 * Created by: NPTAN (05/05/2022)
	 * Version: 1.0
	 */
	@GetMapping
	public ModelAndView payrollPage() {
		ModelAndView modelAndView = new ModelAndView("admin/payroll");
		List<PayrollResponse> payrolls = payrollService.getAlls();
		modelAndView.addObject("payrolls", payrolls);
		return modelAndView;
	}
	
}
