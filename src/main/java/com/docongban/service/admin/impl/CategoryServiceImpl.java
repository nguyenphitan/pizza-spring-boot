package com.docongban.service.admin.impl;

import com.docongban.entity.Category;
import com.docongban.repository.CategoryRepository;
import com.docongban.service.admin.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
