package com.docongban.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Discount;
import com.docongban.entity.Item;
import com.docongban.entity.Product;
import com.docongban.payload.CartResponse;
import com.docongban.repository.DiscountRepository;
import com.docongban.repository.ProductRepository;
import com.docongban.service.CartService;

@Component
public class CartServiceImpl implements CartService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private DiscountRepository discountRepository;
	
	public List<Item> getItemProduct(ArrayList<Item> itemList){
		List<Item> items = new ArrayList<>();
		Product product = new Product();
		
		if(itemList.size()>0) {
			for(Item i: itemList) {
				product=productRepository.findProductById(i.getId());
					
				Item item=new Item();
				
				item.setId(product.getId());
				item.setTitle(product.getTitle());
				item.setPrice(product.getPrice());
				item.setThumbnail(product.getThumbnail());
				item.setContent(product.getContent());
				item.setQuantity(i.getQuantity());
				item.setPrices(i.getQuantity()*product.getPrice()); 
				
				items.add(item);
			}
		}
		
		return items;
	}
	
	public long getTotalCart(ArrayList<Item> itemList) {
		long sum=0;
		Product product = new Product();
		
		if(itemList.size()>0) {
			for(Item i: itemList) {
				product = productRepository.findProductById(i.getId());
				
				sum+=product.getPrice()*i.getQuantity();
			}
		}
		
		return sum;
	}
	
	
	/*
	 * Tính toán các giá trị discount
	 * Created by: NPTAN (19/05/2022)
	 * Version: 1.0
	 */
	public void handleDiscount(ModelAndView modelAndView, List<CartResponse> listCartResponses, HttpServletRequest request) {
		// Tính tổng tiền trong giỏ hàng -> gợi ý discount (DiscountService)
		Double totalCart = 0D;
		for(CartResponse cart : listCartResponses) {
			Long productPrice = cart.getProduct().getPrice();
			Integer quantity = cart.getQuantity();
			Long totalPrice = quantity * productPrice;
			totalCart += totalPrice;
		}
		
		// Gợi ý discount:
		// 1. Lấy ra discount hiện tại: (nếu có)
		Double currentDiscount = 0D;
		Double nextDiscount = 0D;
		Double nextValue = 0D;
		List<Discount> discounts = discountRepository.findAll();
		discounts.sort(Comparator.comparing(Discount::getValue));
		
		if( totalCart < discounts.get(0).getValue() ) {
			nextDiscount = discounts.get(0).getDiscount();
			nextValue = discounts.get(0).getValue();
		} else {
			for ( int i = 1 ; i < discounts.size() ; i++ ) {
				if( totalCart >= discounts.get(i-1).getValue() && totalCart < discounts.get(i).getValue() ) {
					currentDiscount = discounts.get(i-1).getDiscount();
					nextDiscount = discounts.get(i).getDiscount();
					nextValue = discounts.get(i).getValue();
					break;
				}
			}
		}
		
		// 2. Tính toán lại tổng tiền sau khi trừ discount:
		Double discountValue = (totalCart * currentDiscount)/100;
		Long realCart = Math.round(totalCart - discountValue);
		
		// 3. Gợi ý mua thêm xx tiền để đạt discount tiếp theo:
		Double valueToNextDiscount = nextValue - totalCart; 
		
		modelAndView.addObject("currentDiscount", currentDiscount);
		modelAndView.addObject("nextDiscount", nextDiscount);
		modelAndView.addObject("discountValue", discountValue);
		modelAndView.addObject("valueToNextDiscount", valueToNextDiscount);
		modelAndView.addObject("totalCart", totalCart);
		modelAndView.addObject("realCart", realCart);
		
		// Đưa số tiền phải thanh toán lên session:
		HttpSession session = request.getSession();
		session.setAttribute("realCart", realCart);
	}

	
}
