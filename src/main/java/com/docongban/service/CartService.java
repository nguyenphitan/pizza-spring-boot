package com.docongban.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.docongban.entity.Item;
import com.docongban.payload.CartResponse;

@Service
public interface CartService {

	List<Item> getItemProduct(ArrayList<Item> itemList);
	
	// get total cart price
	long getTotalCart(ArrayList<Item> itemList);
	
	// add to cart
	RedirectView addToCart(Model model, int id, HttpSession session);
	
	// get all cart
	void getAllCarts(ModelAndView modelAndView, HttpServletRequest request);
	
	// desc
	void quantityDecCart(Model model, int id, HttpSession session);
	
	// inc
	void quantityIncCart(Model model, int id, HttpSession session);
	
	// remove
	void removeProductCart(Model model, int id, HttpSession session);
	
	// checkout
	String checkOut(Model model, HttpSession session);
	
	// payment online
	ModelAndView payment(Long amount, HttpSession session);
	
	// handle discount
	void handleDiscount(ModelAndView modelAndView, List<CartResponse> listCartResponses, HttpServletRequest request);
}
