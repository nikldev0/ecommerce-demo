package com.ecommerce.demo.controller;

import java.util.Optional;

import com.ecommerce.demo.common.CustomApiResponse;
import com.ecommerce.demo.dto.cart.AddToCartDto;
import com.ecommerce.demo.dto.cart.CartDto;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.service.AuthenticationService;
import com.ecommerce.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    private final AuthenticationService authenticationService;

    @Autowired
    public CartController(CartService cartService, AuthenticationService authenticationService) {
        this.cartService = cartService;
        this.authenticationService = authenticationService;
    }

    // post cart api
    @PostMapping("/add")
    public ResponseEntity<CustomApiResponse> addToCart(@RequestBody AddToCartDto addToCartDto,
                                                       @RequestParam("token") String token) {
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        Optional<User> user = authenticationService.getUser(token);

        cartService.addToCart(addToCartDto, user.get());

        return new ResponseEntity<>(new CustomApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }

    // get all cart items for a user
    @GetMapping("/all")
    public ResponseEntity<CartDto> getCartItems(@RequestParam("token") String token) {
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        Optional<User> user = authenticationService.getUser(token);

        // get cart items

        CartDto cartDto = cartService.listCartItems(user.get());
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }

    // delete a cart item for a user

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<CustomApiResponse> deleteCartItem(@PathVariable("cartItemId") Integer itemId,
                                                      @RequestParam("token") String token) {

        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        Optional<User> user = authenticationService.getUser(token);

        cartService.deleteCartItem(itemId, user.get());

        return new ResponseEntity<>(new CustomApiResponse(true, "Item has been removed"), HttpStatus.OK);

    }



}
