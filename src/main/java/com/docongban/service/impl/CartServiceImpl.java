package com.docongban.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Account;
import com.docongban.entity.Category;
import com.docongban.entity.Discount;
import com.docongban.entity.Item;
import com.docongban.entity.Order;
import com.docongban.entity.OrderAccount;
import com.docongban.entity.OrderDetail;
import com.docongban.entity.Product;
import com.docongban.payload.CartResponse;
import com.docongban.repository.CategoryRepository;
import com.docongban.repository.DiscountRepository;
import com.docongban.repository.OrderAccountRepository;
import com.docongban.repository.OrderDetailRepository;
import com.docongban.repository.OrderRepository;
import com.docongban.repository.ProductRepository;
import com.docongban.service.CartService;

@Component
public class CartServiceImpl implements CartService {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	DiscountRepository discountRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderAccountRepository orderAccountRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	public List<Item> getItemProduct(ArrayList<Item> itemList) {
		List<Item> items = new ArrayList<>();
		Product product = new Product();

		if (itemList.size() > 0) {
			for (Item i : itemList) {
				product = productRepository.findProductById(i.getId());

				Item item = new Item();

				item.setId(product.getId());
				item.setTitle(product.getTitle());
				item.setPrice(product.getPrice());
				item.setThumbnail(product.getThumbnail());
				item.setContent(product.getContent());
				item.setQuantity(i.getQuantity());
				item.setPrices(i.getQuantity() * product.getPrice());

				items.add(item);
			}
		}

		return items;
	}

	public long getTotalCart(ArrayList<Item> itemList) {
		long sum = 0;
		Product product = new Product();

		if (itemList.size() > 0) {
			for (Item i : itemList) {
				product = productRepository.findProductById(i.getId());

				sum += product.getPrice() * i.getQuantity();
			}
		}

		return sum;
	}

	/*
	 * Tính toán các giá trị discount Created by: NPTAN (19/05/2022) Version: 1.0
	 */
	public void handleDiscount(ModelAndView modelAndView, List<CartResponse> listCartResponses,
			HttpServletRequest request) {
		// Tính tổng tiền trong giỏ hàng -> gợi ý discount (DiscountService)
		Double totalCart = 0D;
		for (CartResponse cart : listCartResponses) {
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

		if (totalCart < discounts.get(0).getValue()) {
			nextDiscount = discounts.get(0).getDiscount();
			nextValue = discounts.get(0).getValue();
		} else {
			for (int i = 1; i < discounts.size(); i++) {
				if (totalCart >= discounts.get(i - 1).getValue() && totalCart < discounts.get(i).getValue()) {
					currentDiscount = discounts.get(i - 1).getDiscount();
					nextDiscount = discounts.get(i).getDiscount();
					nextValue = discounts.get(i).getValue();
					break;
				}
			}
		}

		// 2. Tính toán lại tổng tiền sau khi trừ discount:
		Double discountValue = (totalCart * currentDiscount) / 100;
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

	// Add to cart
	@Override
	public Boolean addToCart(Model model, int id, HttpSession session) {
		// get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);

		ArrayList<Item> itemList = new ArrayList<>();
		Item item = new Item();
		item.setId(id);
		item.setQuantity(1);

		ArrayList<Item> item_list = (ArrayList<Item>) session.getAttribute("item-list");
		if (item_list == null) {
			itemList.add(item);
			List<Item> items = getItemProduct(itemList);
			session.setAttribute("itemsSession", items);
			session.setAttribute("item-list", itemList);
			session.setAttribute("cartSize", itemList.size());
			session.setMaxInactiveInterval(60 * 60 * 24);
			return true;
		} else {
			boolean exist = false;
			for (Item i : item_list) {
				if (i.getId() == id) {

					exist = true;
					int quantity = i.getQuantity();
					quantity++;
					i.setQuantity(quantity);
					break;
				}
			}
			if (!exist) {
				item_list.add(item);
			}
		}

		List<Item> items = getItemProduct(item_list);
		session.setAttribute("itemsSession", items);

		session.setAttribute("item-list", item_list);
		session.setAttribute("cartSize", item_list.size());

		return true;
	}

	@Override
	public void getAllCarts(ModelAndView modelAndView, HttpServletRequest request) {
		HttpSession session = request.getSession();
		// get all category
		List<Category> categoris = categoryRepository.findAll();
		modelAndView.addObject("categoris", categoris);

		ArrayList<Item> item_list = (ArrayList<Item>) session.getAttribute("item-list");
		List<Item> items = null;
		int cartSize = 0;
		if (item_list != null) {

			items = getItemProduct(item_list);
			long totalPrice = getTotalCart(item_list);

			modelAndView.addObject("items", items);
			modelAndView.addObject("totalPrice", totalPrice);

			cartSize = items.size();
			session.setAttribute("itemsSession", items);
		} else {
			cartSize = 0;
		}
		session.setAttribute("cartSize", cartSize);
		List<CartResponse> cartResponses = new ArrayList<CartResponse>();
		if (items != null) {
			for (Item item : items) {
				Product product = new Product(item.getId(), item.getTitle(), item.getPrice(), item.getThumbnail(),
						item.getContent(), item.getCategory(), item.getId());
				CartResponse cartResponse = new CartResponse(product, item.getQuantity());
				cartResponses.add(cartResponse);
			}

			handleDiscount(modelAndView, cartResponses, request);
		}
	}

	// desc
	@Override
	public void quantityDecCart(Model model, int id, HttpSession session) {
		// get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		ArrayList<Item> item_list = (ArrayList<Item>) session.getAttribute("item-list");

		for (Item i : item_list) {
			if (i.getId() == id && i.getQuantity() > 1) {
				int quantity = i.getQuantity();
				quantity--;
				i.setQuantity(quantity);
				break;
			}
		}
	}

	// inc
	@Override
	public void quantityIncCart(Model model, int id, HttpSession session) {
		// get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		ArrayList<Item> item_list = (ArrayList<Item>) session.getAttribute("item-list");

		for (Item i : item_list) {
			if (i.getId() == id) {
				int quantity = i.getQuantity();
				quantity++;
				i.setQuantity(quantity);
				break;
			}
		}
	}

	@Override
	public Integer removeProductCart(Model model, int id, HttpSession session) {
		// get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		ArrayList<Item> item_list = (ArrayList<Item>) session.getAttribute("item-list");

		if (item_list != null) {
			for (Item i : item_list) {
				if (i.getId() == id) {
					item_list.remove(item_list.indexOf(i));
					return 1;
				}
			}
		}
		return 0;
	}

	@Override
	public String checkOut(Model model, HttpSession session) {
		// get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		ArrayList<Item> item_list = (ArrayList<Item>) session.getAttribute("item-list");
		List<Item> items = null;
		Account accountSession = (Account) session.getAttribute("account");

		Date d = new Date();
		if (item_list != null && accountSession != null) {

			items = getItemProduct(item_list);

			OrderAccount orderAccount = new OrderAccount();
			orderAccount.setAccountId(accountSession.getId());
			orderAccount.setAccountFullname(accountSession.getFullname());
			orderAccount.setAccountPhone(accountSession.getPhone());
			orderAccount.setAccountEmail(accountSession.getEmail());
			orderAccount.setAccountAddress(accountSession.getAddress());
			orderAccount.setOrderDate(new java.sql.Timestamp(d.getTime()));
			orderAccountRepository.save(orderAccount);

			for (Item i : items) {

				Order order = new Order();

				order.setProductId(i.getId());
				order.setAccountId(accountSession.getId());
				order.setOrderAccountId(orderAccountRepository.getMaxId());
				order.setQuantiy(i.getQuantity());
				order.setOrderDate(new java.sql.Timestamp(d.getTime()));

				orderRepository.save(order);

				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setProductTitle(i.getTitle());
				orderDetail.setProductThumbnail(i.getThumbnail());
				orderDetail.setProductContent(i.getContent());
				orderDetail.setProductPrice(i.getPrice());
				orderDetail.setOrderQuantity(i.getQuantity());
				orderDetail.setOrderAccountId(orderAccountRepository.getMaxId());
				orderDetailRepository.save(orderDetail);
			}
			item_list.clear();
			session.setAttribute("cartSize", 0);
			session.removeAttribute("item-list");
			return "complete";
		} else {
			if (accountSession == null) {
				return "login";
			} else {
				return "cart";
			}
		}
	}

	@Override
	public ModelAndView payment(Long amount, HttpSession session) {
		// Check token khi thanh toán:
		String token = (String) session.getAttribute("token");
		if (token == null) {
			return new ModelAndView("redirect:/auth/login");
		}

		ModelAndView modelAndView = new ModelAndView("vnpay");
		modelAndView.addObject("amount", amount);
		return modelAndView;
	}

}
