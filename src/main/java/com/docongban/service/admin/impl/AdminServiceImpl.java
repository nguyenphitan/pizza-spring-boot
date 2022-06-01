package com.docongban.service.admin.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Discount;
import com.docongban.entity.OrderAccount;
import com.docongban.entity.OrderDetail;
import com.docongban.repository.OrderAccountRepository;
import com.docongban.repository.OrderDetailRepository;
import com.docongban.service.admin.AdminService;
import com.docongban.service.admin.DiscountService;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private DiscountService discountService;

	@Autowired
	private OrderAccountRepository orderAccountRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Override
	public void getAllBills(ModelAndView modelAndView) {
		List<Discount> discounts = discountService.getAlls();

        List<OrderAccount> orderAccounts = orderAccountRepository.findAll();
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        List<Double> totalPrice = new ArrayList<>();

        for (OrderAccount oc : orderAccounts) {
            Double total = 0D;
            for (OrderDetail od : orderDetails) {
                if (oc.getId() == od.getOrderAccountId()) {
                    total += (od.getProductPrice() * od.getOrderQuantity());
                }
            }
            for(int i = 0 ; i < discounts.size() ; i++) {
				Double nextValue = discounts.get(i).getValue();
				if( total < nextValue ) {
					if( i > 0 ) {
						total -= (total * discounts.get(i-1).getDiscount()/100);
						break;						
					}
					else {
						break;
					}
				}
			}
            totalPrice.add(total);
        }

        modelAndView.addObject("orderAccounts", orderAccounts);
        modelAndView.addObject("orderDetails", orderDetails);
        modelAndView.addObject("totalPrice", totalPrice);
	}

}
