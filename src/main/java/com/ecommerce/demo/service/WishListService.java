package com.ecommerce.demo.service;

import java.util.ArrayList;
import java.util.List;

import com.ecommerce.demo.dto.ProductDto;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.model.WishList;
import com.ecommerce.demo.repository.WishListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListService {
    private final WishListRepo wishListRepo;
    private final ProductService productService;

    @Autowired
    public WishListService(WishListRepo wishListRepo, ProductService productService) {
        this.wishListRepo = wishListRepo;
        this.productService = productService;
    }

    public void createWishlist(WishList wishList) {
        wishListRepo.save(wishList);
    }

    public List<ProductDto> getWishListForUser(User user) {
        final List<WishList> wishLists = wishListRepo.findAllByUserOrderByCreatedDateDesc(user);
        List<ProductDto> productDtos = new ArrayList<>();
        for (WishList wishList: wishLists) {
            productDtos.add(productService.convertToDto(wishList.getProduct()));
        }

        return productDtos;
    }
}
