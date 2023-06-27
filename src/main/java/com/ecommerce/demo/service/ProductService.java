package com.ecommerce.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ecommerce.demo.dto.ProductDto;
import com.ecommerce.demo.exceptions.ProductDoesNotExistException;
import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public void createProduct(ProductDto productDto, Category category) {
        Product product = new Product();
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setName(productDto.getName());
        product.setCategory(category);
        product.setPrice(productDto.getPrice());
        productRepo.save(product);
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepo.findAll();

        // convert each product to a DTO and collect the result in a list
        List<ProductDto> productDtos = products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return productDtos;
    }

    public ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setDescription(product.getDescription());
        productDto.setImageURL(product.getImageURL());
        productDto.setName(product.getName());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setPrice(product.getPrice());
        productDto.setId(product.getId());
        return productDto;
    }

    public void updateProduct(ProductDto productDto, Integer productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + productId + " not found"));

        // Map fields from the productDto to the product entity
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());

        // Save the updated product to the database
        productRepo.save(product);
    }


    public Product findById(Integer productId) throws ProductDoesNotExistException {
        Optional<Product> optionalProduct = productRepo.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new ProductDoesNotExistException("product id is invalid: " + productId);
        }
        return optionalProduct.get();
    }


    public void deleteProduct(Integer productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + productId + " not found"));

        productRepo.delete(product);
    }
}
