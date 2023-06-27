package com.ecommerce.demo.exceptions;

public class ProductDoesNotExistException extends IllegalArgumentException {
    public ProductDoesNotExistException(String msg) {
        super(msg);
    }
}
