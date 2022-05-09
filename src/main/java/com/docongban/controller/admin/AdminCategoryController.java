package com.docongban.controller.admin;

import com.docongban.entity.Category;
import com.docongban.repository.CategoryRepository;
import com.docongban.service.admin.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin")
public class AdminCategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/category")
    public ModelAndView getCategories() {
        ModelAndView modelAndView = new ModelAndView("admin/category");
        List<Category> categories = categoryService.getAllCategories();
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("/category/create")
    public ModelAndView createCategoryView() {
        return new ModelAndView("admin/formCategory");
    }

    @PostMapping("/category/create")
    public ModelAndView createCategory(@ModelAttribute("category") Category category) {
        Date date=new Date();
        category.setCreatedAt(new java.sql.Timestamp(date.getTime()));
        category.setUpdatedAt(new java.sql.Timestamp(date.getTime()));
        categoryRepository.save(category);
        return new ModelAndView("redirect:"+"/admin/category");
    }

    @GetMapping("/category/{id}/update")
    public ModelAndView updateCategoryView(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("admin/formCategory");
        Category foundedCategory = categoryRepository.getById(id);
        modelAndView.addObject("updateCategory", foundedCategory);
        return modelAndView;
    }

    @PostMapping("/category/{id}/update")
    public ModelAndView updateCategory(@PathVariable int id, @ModelAttribute("updateCategoryName") String  newCategoryName) {
        Date date=new Date();
        categoryRepository.findById(id)
                .map(category -> {
                    category.setName(newCategoryName);
                    category.setUpdatedAt(new java.sql.Timestamp(date.getTime()));
                    return categoryRepository.save(category);
                })
                .orElseGet(() -> {
                    return null;
                });
        return new ModelAndView("redirect:"+"/admin/category");
    }

    @GetMapping("/category/{id}/delete")
    public ModelAndView deleteCategory(@PathVariable int id) {
        Category deleteCategory = categoryRepository.findById(id).get();
        categoryRepository.delete(deleteCategory);
        return new ModelAndView("redirect:"+"/admin/category");
    }
}
