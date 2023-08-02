package com.docongban.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	CartService cartService;
	
	//add to cart
	@PostMapping("/addToCart/{id}")
	public ResponseEntity<?> getProductByIdToCart(Model model, @PathVariable int id, HttpSession session) {
		return ResponseEntity.ok(cartService.addToCart(model, id, session));
	}
	
	// get all cart
	@GetMapping()
	public ModelAndView cartProduct(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("cart");
		cartService.getAllCarts(modelAndView, request);
		return modelAndView;
	}
	
	@PostMapping("/quantity-dec/{id}")
	public ResponseEntity<?> quantityDecCart(Model model,  @PathVariable int id, HttpSession session) {
		cartService.quantityDecCart(model, id, session);
		return ResponseEntity.ok(id);
	}
	
	@PostMapping("/quantity-inc/{id}")
	public ResponseEntity<?> quantityIncCart(Model model,  @PathVariable int id, HttpSession session) {
		cartService.quantityIncCart(model, id, session);
		return ResponseEntity.ok(id);
	}
	
	@DeleteMapping("/removeProductCart/{id}")
	public ResponseEntity<?> removeProductCart(Model model,  @PathVariable int id, HttpSession session) {
		return ResponseEntity.ok(cartService.removeProductCart(model, id, session));
	}
	
	@GetMapping("/check-out")
	public String checkOut(Model model, HttpSession session) {
		return cartService.checkOut(model, session);
	}
	
	/*
	 * Hiển thị trang thanh toán online
	 * Created by: NPTAN (13/05/2022)
	 * Version: 1.0
	 */
	@GetMapping("/payment/{amount}")
	public ModelAndView payment(@PathVariable("amount") Long amount, HttpSession session) {
		return cartService.payment(amount, session);
	}
}
