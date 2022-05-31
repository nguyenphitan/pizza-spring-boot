package com.docongban.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Item;
import com.docongban.payload.CartResponse;

@Service
public interface CartService {

	List<Item> getItemProduct(ArrayList<Item> itemList);
	
	//get total cart price
	long getTotalCart(ArrayList<Item> itemList);
	
	void handleDiscount(ModelAndView modelAndView, List<CartResponse> listCartResponses, HttpServletRequest request);
}
