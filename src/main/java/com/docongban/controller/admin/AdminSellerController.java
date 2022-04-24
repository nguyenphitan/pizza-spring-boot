package com.docongban.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Account;
import com.docongban.repository.AccountRepository;
import com.docongban.service.admin.SellerService;

@RestController
@RequestMapping("/admin")
public class AdminSellerController {
	
	@Autowired
	SellerService sellerService;
	
	@Autowired
	AccountRepository accountRepository;
	
	/*
	 * Hiển thị ra danh sách nhân viên
	 * Created by: NPTAN (24/04/2022)
	 * Version: 1.0
	 */
	@GetMapping("/seller")
	public ModelAndView getSellers() {
		ModelAndView modelAndView = new ModelAndView("admin/seller");
		List<Account> sellers = sellerService.getAllSellers();
		modelAndView.addObject("sellers", sellers);
		return modelAndView;
	}
	
	/*
	 * Thêm nhân viên (add role SELLER cho tài khoản)
	 * Created by: NPTAN (24/04/2022)
	 * Version: 1.0
	 */
	@PostMapping("/seller")
	public Integer addSeller(@RequestBody int id) {
		Integer response = sellerService.addSeller(id);
		return response;
	}
	
	/*
	 * Xóa nhân viên (chỉ xóa role, không xóa tài khoản)
	 * Created by: NPTAN (24/04/2022)
	 * Version: 1.0 
	 */
	@DeleteMapping("/seller/{id}")
	public void deleteSeller(@PathVariable("id") int id) {
		sellerService.deleteSeller(id);
	}
	
}
