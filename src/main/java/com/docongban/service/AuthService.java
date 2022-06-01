package com.docongban.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.payload.RegisterRequest;
import com.docongban.payload.UpdateAccountRequest;

public interface AuthService {

	// Get register
	String getRegister(Model model, HttpServletRequest request);
	
	String createAccount(Model model, String fullname, String email, String address, String phone, String password);
	
	// Get login
	String getLogin(Model model, HttpServletRequest request);
	
	// Check login
	String checkLogin(Model model, String phone, String password, HttpServletRequest request) throws Exception;
	
	String handleLogin(String phone, String password, HttpServletRequest request);
	
	// Logout
	void handleLogout(HttpServletRequest request, HttpServletResponse response);
	
	// Update account
	void handleUpdateAccount(UpdateAccountRequest updateAccountRequest);
	
	ModelAndView purchaseHistory(Model model, @PathVariable int id);
	
	void handleRegister(RegisterRequest registerRequest);
		
}
