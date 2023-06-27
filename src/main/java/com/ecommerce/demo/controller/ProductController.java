package com.ecommerce.demo.controller;

import java.util.List;

import com.ecommerce.demo.common.CustomApiResponse;
import com.ecommerce.demo.dto.ProductDto;
import com.ecommerce.demo.repository.CategoryRepo;
import com.ecommerce.demo.repository.ProductRepo;
import com.ecommerce.demo.service.ProductService;
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
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    @Autowired
    public ProductController(ProductService productService, CategoryRepo categoryRepo, ProductRepo productRepo) {
        this.productService = productService;
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }


    @Operation(summary = "Add a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product has been added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Category does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))})
    })
    @PostMapping("/add")
    public ResponseEntity<CustomApiResponse> createProduct(@RequestBody ProductDto productDto) {
        return categoryRepo.findById(productDto.getCategoryId())
                .map(category -> {
                    productService.createProduct(productDto, category);
                    return ResponseEntity.status(HttpStatus.CREATED).body(new CustomApiResponse(true, "Product has been added"));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomApiResponse(false, "Category does not exist")));
    }


    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class))}),
            @ApiResponse(responseCode = "204", description = "No products available",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }



    @PostMapping("/update/{productId}")
    public ResponseEntity<CustomApiResponse> updateProduct(@PathVariable("productId") Integer productId, @RequestBody ProductDto productDto) throws Exception {
        return categoryRepo.findById(productDto.getCategoryId())
                .map(category -> {
                    productService.updateProduct(productDto, productId);
                    return ResponseEntity.status(HttpStatus.CREATED).body(new CustomApiResponse(true, "Product has been updated"));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomApiResponse(false, "Category does not exist")));
    }

    @PostMapping("/delete/{productId}")
    public ResponseEntity<CustomApiResponse> deleteProduct(@PathVariable("productId") Integer productId) throws Exception {
        return productRepo.findById(productId)
                .map(category -> {
                    productService.deleteProduct(productId);
                    return ResponseEntity.status(HttpStatus.CREATED).body(new CustomApiResponse(true, "Product has been deleted"));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomApiResponse(false, "Product does not exist")));
    }




}
