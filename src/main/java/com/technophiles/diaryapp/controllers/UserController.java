package com.technophiles.diaryapp.controllers;

import com.technophiles.diaryapp.controllers.reponses.ApiResponse;
import com.technophiles.diaryapp.dtos.UserDto;
import com.technophiles.diaryapp.exceptions.DiaryApplicationException;
import com.technophiles.diaryapp.services.UserService;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/v3/diaryApp")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        return "Welcome to my application";
    }
    @PostMapping("/users/create")
    public ResponseEntity<?> createUser(@RequestParam @Valid @NotNull @NotBlank String email, @RequestParam @Valid @NotBlank @NotNull String password) throws DiaryApplicationException {
//        try {
            UserDto userDto = userService.createAccount(email, password);
            ApiResponse apiResponse = ApiResponse.builder()
                    .payload(userDto)
                    .isSuccessful(true)
                    .statusCode(201)
                    .message("user created successfully")
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
//        } catch (DiaryApplicationException e) {
//            ApiResponse apiResponse = ApiResponse.builder()
//                    .message(e.getMessage())
//                    .isSuccessful(false)
//                    .statusCode(400)
//                    .build();
//            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
//        }
    }
}
