package com.technophiles.diaryapp.controllers;

import com.technophiles.diaryapp.controllers.reponses.AuthToken;
import com.technophiles.diaryapp.controllers.requests.LoginRequest;
import com.technophiles.diaryapp.models.User;
import com.technophiles.diaryapp.security.jwt.TokenProvider;
import com.technophiles.diaryapp.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v3/diaryApp/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword())
        );
        log.info("Authentication --> {}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = tokenProvider.generateJWTToken(authentication);
        User user = userService.findUserByEmail(loginRequest.getEmail());
        return new ResponseEntity<>(new AuthToken(token, user.getId()), HttpStatus.OK);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }));
        return errors;
    }
}
