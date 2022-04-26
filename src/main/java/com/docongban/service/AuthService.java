package com.docongban.service;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.docongban.custom.CustomUserDetails;
import com.docongban.entity.Account;
import com.docongban.jwt.JwtTokenProvider;
import com.docongban.payload.LoginRequest;
import com.docongban.payload.RegisterRequest;
import com.docongban.repository.AccountRepository;

@Service
public class AuthService {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AccountRepository accountRepository;
	
	/*
	 * Xử lý đăng nhập -> Tạo mã token
	 * Created by: NPTAN (22/04/2022)
	 * Version: 1.0
	 */
	public String handleLogin(String phone, String password, HttpServletRequest request) {
		String jwt = null;
		try {
			// Tạo ra LoginRequest từ username và password nhận được từ client
			LoginRequest loginRequest = new LoginRequest(phone, password);
			// Xác thực thông tin người dùng Request lên:
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequest.getPhone(), 
							loginRequest.getPassword()
					)
			);
			
			// Nếu không xảy ra exception tức là thông tin hợp lệ
			// Set thông tin authentication vào Security Context
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			// Trả về jwt cho người dùng
			jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
			
			// Lưu jwt vào session:
			HttpSession session = request.getSession();
			session.setAttribute("token", jwt);
			
			// Lấy ra id người dùng -> lấy ra role -> đưa lên session:
			Integer accountId = tokenProvider.getAccountIdFromJWT(jwt);
			Account account = accountRepository.getById(accountId);
			String roleAccount = account.getRole();
			session.setAttribute("roleAccount", roleAccount);
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return jwt;
	}
	
	
	/*
	 * Xử lý đăng ký
	 * Created by: NPTAN (22/04/2022)
	 * Version: 1.0
	 */
	public void handleRegister(RegisterRequest registerRequest) {
		Date d=new Date();
		Account account = new Account();
		account.setCreatedAt(new java.sql.Timestamp(d.getTime()));
		account.setUpdatedAt(new java.sql.Timestamp(d.getTime()));
		account.setFullname(registerRequest.getFullname());
		account.setAddress(registerRequest.getAddress());
		account.setEmail(registerRequest.getEmail());
		account.setPhone(registerRequest.getPhone());
		account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		account.setRole("USER");
		accountRepository.save(account);
	}
	
	/*
	 * Xử lý đăng xuất
	 * Created by: NPTAN (23/04/2022)
	 * Version: 1.0
	 */
	public void handleLogout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		// Xóa token khỏi session:
		session.removeAttribute("token");
		// Xóa account khỏi session:
		session.removeAttribute("account");
		// Xóa fullname khỏi session:
		session.removeAttribute("fullname");
		// Xóa role:
		session.removeAttribute("roleAccount");
		// Xóa tất cả cookies:
		for (Cookie cookie : request.getCookies()) {
		    cookie.setValue("");
		    cookie.setMaxAge(0);
		    cookie.setPath("/");

		    response.addCookie(cookie);
		}
	}
	
	
}
