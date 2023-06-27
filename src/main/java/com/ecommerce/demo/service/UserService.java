package com.ecommerce.demo.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import com.ecommerce.demo.dto.ResponseDto;
import com.ecommerce.demo.dto.user.SignInDto;
import com.ecommerce.demo.dto.user.SignInReponseDto;
import com.ecommerce.demo.dto.user.SignupDto;
import com.ecommerce.demo.exceptions.AuthenticationFailException;
import com.ecommerce.demo.exceptions.CustomException;
import com.ecommerce.demo.model.AuthenticationToken;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.UserRepo;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepo userRepo;

    private final AuthenticationService authenticationService;

    @Autowired
    public UserService(UserRepo userRepo, AuthenticationService authenticationService) {
        this.userRepo = userRepo;
        this.authenticationService = authenticationService;
    }

    @Transactional
    public ResponseDto signUp(SignupDto signupDto) {
        User existingUser = userRepo.findByEmail(signupDto.email());
        if (existingUser != null) {
            throw new CustomException("User already present");
        }
        try {
            String encryptedPassword = hashPassword(signupDto.password());
            User user = new User(signupDto.firstName(), signupDto.lastName(),
                    signupDto.email(), encryptedPassword);
            userRepo.save(user);
            AuthenticationToken authenticationToken = new AuthenticationToken(user);
            authenticationService.saveConfirmationToken(authenticationToken);
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("Password encryption failed");
        }

        return new ResponseDto("success", "User created successfully");
    }


    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return hash;
    }

    public SignInReponseDto signIn(SignInDto signInDto) {
        // find user by email
        User user = Optional.ofNullable(userRepo.findByEmail(signInDto.email()))
                .orElseThrow(() -> new AuthenticationFailException("User is not valid"));

        // Check if password in db matches the one provided
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.password()))) {
                throw new AuthenticationFailException("Wrong password");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Retrieve the token
        String token = Optional.ofNullable(authenticationService.getToken(user))
                .map(AuthenticationToken::getToken)
                .orElseThrow(() -> new CustomException("Token is absent"));

        // Return response
        return new SignInReponseDto("success", token);
    }

}
