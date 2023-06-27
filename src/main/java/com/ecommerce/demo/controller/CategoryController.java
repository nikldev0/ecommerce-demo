package com.ecommerce.demo.controller;

import java.util.List;

import com.ecommerce.demo.common.CustomApiResponse;
import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Category provided")})
    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>(new CustomApiResponse(true, "New category successfully created"), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a list of all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched list successfully",
                    content = {
                            @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Failed to fetch list")})
    @GetMapping("/list")
    public ResponseEntity<List<Category>> listCategory() {
        List<Category> categories = categoryService.listCategory();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(summary = "Update an existing category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Category does not exist")
    })
    @PostMapping("/update/{categoryId}")
    public ResponseEntity<CustomApiResponse> updateCategory(@PathVariable("categoryId") int categoryId, @RequestBody Category category) {
        return categoryService.findById(categoryId)
                .map(cat -> {
                    categoryService.editCategory(categoryId, category);
                    return new ResponseEntity<>(new CustomApiResponse(true, "Category has been successfully updated"), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(new CustomApiResponse(false, "Category does not exist"), HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Delete an existing category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category deleted successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Category does not exist")
    })
    @PostMapping("/delete/{categoryId}")
    public ResponseEntity<CustomApiResponse> deleteCategory(@PathVariable("categoryId") int categoryId) {
        return categoryService.findById(categoryId)
                .map(cat -> {
                    categoryService.deleteCategory(categoryId);
                    return new ResponseEntity<>(new CustomApiResponse(true, "Category " + categoryId + " has been successfully deleted"), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(new CustomApiResponse(false, "Category does not exist"), HttpStatus.NOT_FOUND));
    }



}
