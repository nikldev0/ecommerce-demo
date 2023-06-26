package com.ecommerce.demo.service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    @Autowired
    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public void createCategory(Category category) {
        categoryRepo.save(category);
    }

    public List<Category> listCategory() {
        return categoryRepo.findAll();
    }


    public void editCategory(int categoryId, Category updateCategory) {
        categoryRepo.findById(categoryId).ifPresentOrElse(
                category -> {
                    category.setCategoryName(updateCategory.getCategoryName());
                    category.setDescription(updateCategory.getDescription());
                    category.setImageUrl(updateCategory.getImageUrl());
                    categoryRepo.save(category);
                },
                () -> {
                    // handle the case where category was not found
                }
        );
    }


    public Optional<Category> findById(int categoryId) {
        return categoryRepo.findById(categoryId);
    }

    public void deleteCategory(int categoryId) {
        categoryRepo.deleteById(categoryId);
    }
}
