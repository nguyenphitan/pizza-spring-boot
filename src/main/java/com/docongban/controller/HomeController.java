package com.docongban.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Banner;
import com.docongban.entity.Category;
import com.docongban.entity.Product;
import com.docongban.repository.BannerRepository;
import com.docongban.repository.CategoryRepository;
import com.docongban.repository.ProductRepository;
import com.docongban.service.ProductService;
import com.docongban.service.VNPayService;
import com.docongban.service.admin.DiscountService;

@Controller
public class HomeController {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private VNPayService vnPayService;
	
	@Autowired
	private DiscountService discountService;
	
	@Autowired
	BannerRepository bannerRepository;
	
	@GetMapping("/")
	public String home(Model model) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		//get banner
		List<Banner> banners = bannerRepository.getViewBanner();
		model.addAttribute("banners", banners);
		
		//get all product
		List<Product> products = productRepository.findAll();
		model.addAttribute("products", products);
		
		return "index";
	}
	
	@GetMapping("/category/{id}")
	public String productByCate(Model model, @PathVariable int id) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		//get all product by category id
		model.addAttribute("products", productService.getAllProductByCategoryId(id));
		
		//get category by id (to show name int html)
		model.addAttribute("category", categoryRepository.getById(id));
		
		return "category";
	}
	
	//search
	@GetMapping("/search")
	public String search(Model model,@RequestParam("keyword") String keyword) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("products", productService.getAllProductBySearchName(keyword));
		
		return "search";
	}
	
	
	/*
	 * Hiển thị trang quản lý mã giảm giá
	 * Created by: NPTAN (19/05/2022)
	 * Version: 1.0
	 */
	@GetMapping("/admin-discount")
	public ModelAndView discountPage(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("admin/discount");
		modelAndView.addObject("discounts", discountService.getAlls());
		return modelAndView;
	}
	
	
	/*
	 * Hiển thị trang thêm mới mã giảm giá
	 * Created by: NPTAN
	 * Version: 1.0
	 */
	@GetMapping("/admin/add-discount")
	public ModelAndView createDiscount() {
		return new ModelAndView("admin/add-discount");
	}
	
	
	/*
	 * Hiển thị thông tin sau khi thanh toán cho khách hàng
	 * Created by: NPTAN
	 * Version: 1.0
	 */
	@GetMapping("/vnpay_return")
	public ModelAndView returnPage(
			@RequestParam("vnp_Amount") String amount,
			@RequestParam("vnp_BankCode") String bankCode,
			@RequestParam("vnp_BankTranNo") String bankTranNo,
			@RequestParam("vnp_CardType") String cardType,
			@RequestParam("vnp_OrderInfo") String orderInfo,
			@RequestParam("vnp_PayDate") String payDate,
			@RequestParam("vnp_ResponseCode") String responseCode,
			@RequestParam("vnp_TmnCode") String tmnCode,
			@RequestParam("vnp_TransactionNo") String transactionNo,
			@RequestParam("vnp_TransactionStatus") String transactionStatus,
			@RequestParam("vnp_TxnRef") String txnRef,
			@RequestParam("vnp_SecureHash") String secureHash
			
	) {
		return vnPayService.vnpayReturnPage(
				amount, bankCode, bankTranNo, cardType, orderInfo, payDate, responseCode, tmnCode, transactionNo, transactionStatus, txnRef, secureHash);
	}
	
}
