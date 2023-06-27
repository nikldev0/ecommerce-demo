package com.ecommerce.demo.controller;

import java.util.List;
import java.util.Optional;

import com.ecommerce.demo.common.CustomApiResponse;
import com.ecommerce.demo.dto.ProductDto;
import com.ecommerce.demo.exceptions.CustomException;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.model.WishList;
import com.ecommerce.demo.service.AuthenticationService;
import com.ecommerce.demo.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wishlist")
public class WishListController {

    private final WishListService wishListService;

    private final AuthenticationService authenticationService;

    @Autowired
    public WishListController(WishListService wishListService, AuthenticationService authenticationService) {
        this.wishListService = wishListService;
        this.authenticationService = authenticationService;
    }

    // save product as wishlist item

    @PostMapping("/add")
    public ResponseEntity<CustomApiResponse> addToWishList(@RequestBody Product product,
                                                           @RequestParam("token") String token) {
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user

        Optional<User> user = authenticationService.getUser(token);

        // save the item in wishlist

        WishList wishList = new WishList(user.get(), product);

        wishListService.createWishlist(wishList);

        CustomApiResponse apiResponse = new CustomApiResponse(true, "Added to wishlist");
        return  new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    // get all wishlist item for a user

    @GetMapping("/{token}")
    public ResponseEntity<List<ProductDto>> getWishList(@PathVariable("token") String token) {

        // authenticate the token
        authenticationService.authenticate(token);

        // find the user

        Optional<User> user = authenticationService.getUser(token);

        List<ProductDto> productDtos = wishListService.getWishListForUser(user.get());

        return new ResponseEntity<>(productDtos, HttpStatus.OK);

    }


}
