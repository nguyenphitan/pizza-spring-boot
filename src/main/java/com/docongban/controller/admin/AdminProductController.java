package com.docongban.controller.admin;

import com.docongban.entity.Category;
import com.docongban.entity.Product;
import com.docongban.repository.CategoryRepository;
import com.docongban.repository.ProductRepository;
import com.docongban.service.ProductService;
import org.aspectj.weaver.NewParentTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepository categoryRepository;

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
        List<Category> categories = categoryRepository.findAll();
        Product newProduct = new Product();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("newProduct", newProduct);
        return modelAndView;
    }

    @PostMapping("/create")
    ModelAndView createProduct(@ModelAttribute("newProduct") Product newProduct) {
        productRepository.save(newProduct);
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
        List<Category> categories = categoryRepository.findAll();
        Product newProduct = new Product();
        Product foundProduct = productRepository.findProductById(id);
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("newProduct", newProduct);
        modelAndView.addObject("updateProduct", foundProduct);
        return modelAndView;
    }

    @PostMapping("/{id}/update")
    ModelAndView updateProduct(@PathVariable Integer id,@ModelAttribute("updateProduct") Product updateProduct) {
        Product toUpdateProduct = productRepository.findProductById(id);
        toUpdateProduct.setTitle(updateProduct.getTitle());
        toUpdateProduct.setThumbnail(updateProduct.getThumbnail());
        toUpdateProduct.setContent(updateProduct.getContent());
        toUpdateProduct.setPrice(updateProduct.getPrice());
        toUpdateProduct.setCategory(updateProduct.getCategory());
        productRepository.save(toUpdateProduct);
        return new ModelAndView("redirect:" + "/admin/products");
    }
}
