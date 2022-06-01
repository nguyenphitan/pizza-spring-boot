package com.docongban.controller.admin;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.docongban.entity.Account;
import com.docongban.repository.AccountRepository;
import com.docongban.repository.OrderAccountRepository;
import com.docongban.repository.OrderDetailRepository;
import com.docongban.service.admin.AdminAccountService;
import com.docongban.service.admin.AdminService;
import com.docongban.service.admin.SellerService;

@RestController
@RequestMapping("/admin")
//@CrossOrigin("http://localhost:8088/admin/**")
public class AdminController {

    @Autowired
    OrderAccountRepository orderAccountRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AdminAccountService adminAccountService;

    @Autowired
    SellerService sellerService;
    
    @Autowired
    private AdminService adminService;

    @GetMapping("/bill")
    public ModelAndView getBill() {
        ModelAndView modelAndView = new ModelAndView("admin/bill");
        adminService.getAllBills(modelAndView);
        return modelAndView;
    }

    @Transactional
    @GetMapping("/bill/complete/{id}")
    public RedirectView checkComplete(@PathVariable int id) {
        orderAccountRepository.updateStatus(id);
        return new RedirectView("/admin/bill");
    }


    /*
     * Hiển thị ra danh sách tất cả người dùng (danh sách tất cả account)
     * Created by: NPTAN (24/04/2022)
     * Version: 1.0
     */
    @GetMapping("/account")
    public ModelAndView getAccounts() {
        ModelAndView modelAndView = new ModelAndView("admin/account");
        List<Account> accounts = adminAccountService.getAllAccounts();
        modelAndView.addObject("accounts", accounts);
        return modelAndView;
    }


}
