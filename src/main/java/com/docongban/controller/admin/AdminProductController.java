package com.docongban.controller.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Product;
import com.docongban.repository.CategoryRepository;
import com.docongban.repository.ProductRepository;
import com.docongban.service.ProductService;
import com.docongban.service.admin.AdminProductService;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {
	
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepository categoryRepository;
    
    @Autowired
    AdminProductService adminProductService;

    @GetMapping("")
    ModelAndView getAllProducts() {
        ModelAndView modelAndView = new ModelAndView("admin/product");
        List<Product> products = productRepository.findAll();
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/create")
    ModelAndView createProductView() {
        ModelAndView modelAndView = new ModelAndView("admin/formProduct");
        modelAndView.addObject("categories", categoryRepository.findAll());
        modelAndView.addObject("newProduct", new Product());
        return modelAndView;
    }

    @PostMapping("/create")
    ModelAndView createProduct(
    		@RequestParam("price") Long price,
    		@RequestParam("title") String title, 
    		@RequestParam("content") String content,
    		@RequestParam("categoryId") Integer categoryId,
    		@RequestParam("thumbnail") MultipartFile multipartFile
    	) throws IOException {
    	adminProductService.create(price, title, content, categoryId, multipartFile);
        return new ModelAndView("redirect:" + "/admin/products");
    }

    @GetMapping("/{id}/delete")
    ModelAndView delete(@PathVariable Integer id) {
        productRepository.deleteById(id);
        return new ModelAndView("redirect:" + "/admin/products");
    }

    @GetMapping("/{id}/update")
    ModelAndView updateProductView(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("admin/formProduct");
        modelAndView.addObject("categories", categoryRepository.findAll());
        modelAndView.addObject("newProduct", new Product());
        modelAndView.addObject("updateProduct", productRepository.findProductById(id));
        return modelAndView;
    }

    @PostMapping("/{id}/update")
    ModelAndView updateProduct(
    		@PathVariable Integer id,
    		@RequestParam("price") Long price,
    		@RequestParam("title") String title, 
    		@RequestParam("content") String content,
    		@RequestParam("categoryId") Integer categoryId,
    		@RequestParam("thumbnail") MultipartFile multipartFile
    ) throws IOException {
    	adminProductService.update(id, price, title, content, categoryId, multipartFile);
        return new ModelAndView("redirect:" + "/admin/products");
    }
}
