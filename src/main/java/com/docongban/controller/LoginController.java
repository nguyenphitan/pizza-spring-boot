package com.docongban.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.docongban.entity.Account;
import com.docongban.entity.Category;
import com.docongban.entity.OrderAccount;
import com.docongban.entity.OrderDetail;
import com.docongban.jwt.JwtTokenProvider;
import com.docongban.payload.RegisterRequest;
import com.docongban.payload.UpdateAccountRequest;
import com.docongban.repository.AccountRepository;
import com.docongban.repository.CategoryRepository;
import com.docongban.repository.OrderAccountRepository;
import com.docongban.repository.OrderDetailRepository;
import com.docongban.service.AccountService;
import com.docongban.service.AuthService;

@Controller
@RequestMapping("/auth")
public class LoginController {

	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired(required = true)
	AccountService accountService;
	
	@Autowired
	OrderAccountRepository orderAccountRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	AuthService authService;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	 
	@GetMapping("/register")
	public String register(Model model, HttpServletRequest request) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		// Kiểm tra token:
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");
		// 1. Nếu đã đăng nhập -> quay về trang chủ
		if( token != null ) {
			return "redirect:/";
		}
		// 2. Nếu chưa đăng nhập -> vào trang đăng ký
		return "register";
	}
	
	@PostMapping("/register")
	public String createAccount(
			Model model, 
			@RequestParam("fullname") String fullname,
			@RequestParam("email") String email, 
			@RequestParam("address") String address, 
			@RequestParam("phone") String phone,
			@RequestParam("password") String password
		) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);

		boolean accountCheck = accountService.checkAccountExisted(phone);
		
		if(!accountCheck ) {
			model.addAttribute("fullname", fullname);
			model.addAttribute("email", email);
			model.addAttribute("address", address);
			model.addAttribute("phone", phone);
			model.addAttribute("phoneExisted", "Số điện thoại đã được đăng kí vui lòng thử với số khác");
			
			return "register";
		}
		
		// Xử lý service liên quan đến đăng ký.
		RegisterRequest registerRequest = new RegisterRequest(phone, password, fullname, email, address, password);
		authService.handleRegister(registerRequest);
		
		return "redirect:/auth/login";
	}
	
	@GetMapping("/login")
	public String login(Model model, HttpServletRequest request) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		// Kiểm tra token:
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");
		// 1. Nếu đã đăng nhập -> quay về trang chủ
		if( token != null ) {
			return "redirect:/";
		}
		// 2. Nếu chưa đăng nhập -> vào trang đăng nhập
		return "login";
	}
	
	@PostMapping("/login")
	public String checkLogin(
			Model model, 
			@RequestParam("phone") String phone, 
			@RequestParam("password") String password,
			HttpServletRequest request
		) throws Exception {
		
		HttpSession session = request.getSession();
		
		//get all category.
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		// Xử lý service liên quan đến đăng nhập.
		String token = authService.handleLogin(phone, password, request);
		
		// Nếu đăng nhập thành công -> lấy ra Account từ jwt:
		if(token != null) {
			Integer accountId = jwtTokenProvider.getAccountIdFromJWT(token);
			Account account = accountRepository.getById(accountId);
			session.setAttribute("account", account);
			session.setAttribute("fullname", account.getFullname());
			session.setMaxInactiveInterval(60*60*24);
			return "redirect:/";
		} else {
			model.addAttribute("phone", phone);
			model.addAttribute("error", "Vui lòng kiểm tra lại số điện thoại hoặc mật khẩu");
		}
		 
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		authService.handleLogout(request, response);
		return "redirect:/auth/login";
	}
	
	@GetMapping("/account/{id}")
	public String accountDetail(Model model, @PathVariable int id) {
		
		//get all category.
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		model.addAttribute("updateAccount", accountRepository.findById(id).get());
		
		return "accountDetail";
	}
	
	@GetMapping("/updateAccount/{id}")
	public String updateAccount(Model model, @PathVariable int id) {
		
		//get all category.
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		model.addAttribute("updateAccount", accountRepository.findById(id).get());
		
		return "updateAccount";
	}
	
	@PostMapping("/updateAccount/save")
	public String handleUpdateAccount(@ModelAttribute("updateAccount") Account account, HttpServletRequest request, HttpServletResponse response) {
		
//		//get datetime now 
//		Date d=new Date();
//		account.setCreatedAt(new java.sql.Timestamp(d.getTime()));
//		account.setUpdatedAt(new java.sql.Timestamp(d.getTime()));
//		accountRepository.save(account);
//		
		UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest(account.getPhone(), account.getPassword(),account.getId(),account.getFullname(), account.getEmail(), account.getAddress(), account.getPassword());
		authService.handleUpdateAccount(updateAccountRequest);
		
		authService.handleLogout(request, response);
		
		return "redirect:/auth/login";
	}
	
	@GetMapping("/history/{id}")
	public ModelAndView purchaseHistory(Model model, @PathVariable int id) {
		ModelAndView modelAndView = new ModelAndView("history");
		
		//get all category.
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		DecimalFormat df = new DecimalFormat(",###");

        List<OrderAccount> orderAccounts = orderAccountRepository.findOrderAccountByAccountId(id);
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        List<String> totalPrice = new ArrayList<>();

        for (OrderAccount oc : orderAccounts) {
            long total = 0;
            for (OrderDetail od : orderDetails) {
                if (oc.getId() == od.getOrderAccountId()) {
                    total += od.getProductPrice()*od.getOrderQuantity();
                }
            }
            totalPrice.add(df.format(total));
        }

        modelAndView.addObject("orderAccounts", orderAccounts);
        modelAndView.addObject("orderDetails", orderDetails);
        modelAndView.addObject("totalPrice", totalPrice);
		
		return modelAndView;
	}	
	
	@Transactional
    @GetMapping("/history/delete/{id}")
    public String  checkComplete(@PathVariable int id, HttpServletRequest request) {
        orderAccountRepository.deleteById(id);
        orderDetailRepository.deleteByOrderAccountId(id);
        return "redirect:" + request.getHeader("Referer");
    }
}