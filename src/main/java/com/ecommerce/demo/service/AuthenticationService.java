package com.ecommerce.demo.service;

import java.util.Objects;
import java.util.Optional;

import com.ecommerce.demo.exceptions.AuthenticationFailException;
import com.ecommerce.demo.model.AuthenticationToken;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.TokenRepo;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final TokenRepo tokenRepo;

    public AuthenticationService(TokenRepo tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        tokenRepo.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {
        return tokenRepo.findByUser(user);
    }

    public Optional<User> getUser(String token) {
        return Optional.ofNullable(tokenRepo.findByToken(token))
                .map(AuthenticationToken::getUser);
    }

    public void authenticate(String token) throws AuthenticationFailException {
        Optional.ofNullable(token)
                .orElseThrow(() -> new AuthenticationFailException("Token not present"));
        getUser(token)
                .orElseThrow(() -> new AuthenticationFailException("Token not valid"));
    }




}
