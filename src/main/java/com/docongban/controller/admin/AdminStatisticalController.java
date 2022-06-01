package com.docongban.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.payload.StatisticalResponse;
import com.docongban.service.admin.StatisticalService;

/*
 * Thống kê doanh thu
 * Created by: NPTAN (02/05/2022)
 * Version: 1.0
 */

@RestController
@RequestMapping("/admin/statistical")
public class AdminStatisticalController {
	
	@Autowired
	private StatisticalService statisticalService;
	
	@GetMapping
	public ModelAndView statisticalPage() {
		ModelAndView modelAndView = new ModelAndView("admin/statistical");
		return modelAndView;
	}
	
	@PostMapping("/{month}")
	public List<StatisticalResponse> getMonthStatistical(@PathVariable("month") String month, HttpServletRequest request) {
		return statisticalService.getMonthStatistical(month, request);
	}
	
	
}
