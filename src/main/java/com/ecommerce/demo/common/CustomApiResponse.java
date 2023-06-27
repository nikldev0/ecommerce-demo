package com.ecommerce.demo.common;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomApiResponse {
    public final boolean success;
    public final String message;

    public CustomApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getTimestamp() {
        return LocalDateTime.now().toString();
    }
}
