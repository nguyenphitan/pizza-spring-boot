package com.docongban.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.rsocket.server.RSocketServer.Transport;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.custom.CustomUserDetails;
import com.docongban.entity.Account;
import com.docongban.entity.Category;
import com.docongban.entity.OrderAccount;
import com.docongban.entity.OrderDetail;
import com.docongban.jwt.JwtTokenProvider;
import com.docongban.payload.LoginRequest;
import com.docongban.payload.RegisterRequest;
import com.docongban.payload.UpdateAccountRequest;
import com.docongban.repository.AccountRepository;
import com.docongban.repository.CategoryRepository;
import com.docongban.repository.OrderAccountRepository;
import com.docongban.repository.OrderDetailRepository;
import com.docongban.service.AccountService;
import com.docongban.service.AuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccountService accountService;

	@Autowired
	JwtTokenProvider tokenProvider;
	
	@Autowired
	OrderAccountRepository orderAccountRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	AuthenticationManager authenticationManager;
	
	private static final String CLIENT_ID = "269633183624-gaqifcncpkoqnt3pg2m165j3dqtrajbk.apps.googleusercontent.com";

	@Override
	public String getRegister(Model model, HttpServletRequest request) {
		model.addAttribute("categoris", categoryRepository.findAll());
		// Kiểm tra token:
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");
		// 1. Nếu đã đăng nhập -> quay về trang chủ
		if (token != null) {
			return "redirect:/";
		}
		// 2. Nếu chưa đăng nhập -> vào trang đăng ký
		return "register";
	}

	@Override
	public void handleRegister(RegisterRequest registerRequest) {
		Date d = new Date();
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

	@Override
	public String createAccount(Model model, String fullname, String email, String address, String phone,
			String password) {
		// get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);

		boolean accountCheck = accountService.checkAccountExisted(phone);

		if (!accountCheck) {
			model.addAttribute("fullname", fullname);
			model.addAttribute("email", email);
			model.addAttribute("address", address);
			model.addAttribute("phone", phone);
			model.addAttribute("phoneExisted", "Số điện thoại đã được đăng kí vui lòng thử với số khác");

			return "register";
		}

		// Xử lý service liên quan đến đăng ký.
		RegisterRequest registerRequest = new RegisterRequest(phone, password, fullname, email, address, password);
		handleRegister(registerRequest);

		return "redirect:/auth/login";
	}

	@Override
	public String getLogin(Model model, HttpServletRequest request) {
		// get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);

		// Kiểm tra token:
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");
		// 1. Nếu đã đăng nhập -> quay về trang chủ
		if (token != null) {
			return "redirect:/";
		}
		// 2. Nếu chưa đăng nhập -> vào trang đăng nhập
		return "login";
	}

	@Override
	public String checkLogin(Model model, String phone, String password, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();

		// get all category.
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);

		// Xử lý service liên quan đến đăng nhập.
		String token = handleLogin(phone, password, request);

		// Nếu đăng nhập thành công -> lấy ra Account từ jwt:
		if (token != null) {
			Integer accountId = tokenProvider.getAccountIdFromJWT(token);
			Account account = accountRepository.getById(accountId);
			session.setAttribute("account", account);
			session.setAttribute("fullname", account.getFullname());
			session.setMaxInactiveInterval(60 * 60 * 24);
			return "redirect:/";
		} else {
			model.addAttribute("phone", phone);
			model.addAttribute("error", "Vui lòng kiểm tra lại số điện thoại hoặc mật khẩu");
		}

		return "login";
	}

	@Override
	public String handleLogin(String phone, String password, HttpServletRequest request) {
		String jwt = null;
		try {
			// Tạo ra LoginRequest từ username và password nhận được từ client
			LoginRequest loginRequest = new LoginRequest(phone, password);
			// Xác thực thông tin người dùng Request lên:
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getPhone(), loginRequest.getPassword()));

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

	@Override
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

	@Override
	public void handleUpdateAccount(UpdateAccountRequest updateAccountRequest) {
		Date d = new Date();
		Account account = new Account();
		account.setId(updateAccountRequest.getId());
		account.setCreatedAt(new java.sql.Timestamp(d.getTime()));
		account.setUpdatedAt(new java.sql.Timestamp(d.getTime()));
		account.setFullname(updateAccountRequest.getFullname());
		account.setAddress(updateAccountRequest.getAddress());
		account.setEmail(updateAccountRequest.getEmail());
		account.setPhone(updateAccountRequest.getPhone());
		account.setPassword(passwordEncoder.encode(updateAccountRequest.getPassword()));
		account.setRole("USER");
		accountRepository.save(account);
	}

	@Override
	public ModelAndView purchaseHistory(Model model, int id) {
		ModelAndView modelAndView = new ModelAndView("history");

		// get all category.
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
					total += od.getProductPrice() * od.getOrderQuantity();
				}
			}
			totalPrice.add(df.format(total));
		}

		modelAndView.addObject("orderAccounts", orderAccounts);
		modelAndView.addObject("orderDetails", orderDetails);
		modelAndView.addObject("totalPrice", totalPrice);

		return modelAndView;
	}

	/**
	 * Verify token from google login.
	 * 
	 * @param token String
	 * @return true/false
	 * @throws IOException 
	 * @throws GeneralSecurityException 
	 */
	@Override
	public Boolean verifyToken(String token) throws GeneralSecurityException, IOException {
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
		    .setAudience(Collections.singletonList(CLIENT_ID))
		    .build();

		// (Receive idTokenString by HTTPS POST)

		GoogleIdToken idToken = verifier.verify(token);
		if (idToken != null) {
		  Payload payload = idToken.getPayload();

		  // Print user identifier
		  String userId = payload.getSubject();
		  System.out.println("User ID: " + userId);

		  // Get profile information from payload
		  String email = payload.getEmail();
		  boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
		  String name = (String) payload.get("name");
		  String pictureUrl = (String) payload.get("picture");
		  String locale = (String) payload.get("locale");
		  String familyName = (String) payload.get("family_name");
		  String givenName = (String) payload.get("given_name");

		  // Use or store profile information
		  // ...

		} else {
		  System.out.println("Invalid ID token.");
		  return false;
		}
		
		return true;
	}
	

}
