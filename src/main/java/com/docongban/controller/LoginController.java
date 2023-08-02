package com.docongban.controller;


import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Account;
import com.docongban.payload.UpdateAccountRequest;
import com.docongban.repository.AccountRepository;
import com.docongban.repository.CategoryRepository;
import com.docongban.repository.OrderAccountRepository;
import com.docongban.repository.OrderDetailRepository;
import com.docongban.service.AuthService;

@Controller
@RequestMapping("/auth")
public class LoginController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	OrderAccountRepository orderAccountRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	AuthService authService;

	@GetMapping("/register")
	public String register(Model model, HttpServletRequest request) {
		return authService.getRegister(model, request);
	}

	@PostMapping("/register")
	public String createAccount(Model model, @RequestParam("fullname") String fullname,
			@RequestParam("email") String email, @RequestParam("address") String address,
			@RequestParam("phone") String phone, @RequestParam("password") String password
	) {
		return authService.createAccount(model, fullname, email, address, phone, password);
	}

	@GetMapping("/login")
	public String login(Model model, HttpServletRequest request) {
		return authService.getLogin(model, request);
	}

	@PostMapping("/login")
	public String checkLogin(Model model, @RequestParam("phone") String phone,
			@RequestParam("password") String password, HttpServletRequest request) throws Exception {
		return authService.checkLogin(model, phone, password, request);
	}
	
	@PostMapping("/verify/{idToken}")
	public ResponseEntity<?> verifyToken(@PathVariable("idToken") String token) throws GeneralSecurityException, IOException {
		return ResponseEntity.ok(authService.verifyToken(token));
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		authService.handleLogout(request, response);
		return "redirect:/auth/login";
	}

	@GetMapping("/account/{id}")
	public String accountDetail(Model model, @PathVariable int id) {
		model.addAttribute("categoris", categoryRepository.findAll());
		model.addAttribute("updateAccount", accountRepository.findById(id).get());
		return "accountDetail";
	}

	@GetMapping("/updateAccount/{id}")
	public String updateAccount(Model model, @PathVariable int id) {
		model.addAttribute("categoris", categoryRepository.findAll());
		model.addAttribute("updateAccount", accountRepository.findById(id).get());
		return "updateAccount";
	}

	@PostMapping("/updateAccount/save")
	public String handleUpdateAccount(@ModelAttribute("updateAccount") Account account, HttpServletRequest request,
			HttpServletResponse response) {
		UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest(account.getPhone(), account.getPassword(),
				account.getId(), account.getFullname(), account.getEmail(), account.getAddress(),
				account.getPassword());
		authService.handleUpdateAccount(updateAccountRequest);
		authService.handleLogout(request, response);
		return "redirect:/auth/login";
	}

	
	@GetMapping("/history/{id}")
	public ModelAndView purchaseHistory(Model model, @PathVariable int id) {
		return authService.purchaseHistory(model, id);
	}	
	
	@Transactional
    @GetMapping("/history/delete/{id}")
    public String  checkComplete(@PathVariable int id, HttpServletRequest request) {
        orderAccountRepository.deleteById(id);
        orderDetailRepository.deleteByOrderAccountId(id);
        return "redirect:" + request.getHeader("Referer");
    }

}