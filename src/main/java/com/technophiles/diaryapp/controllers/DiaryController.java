package com.technophiles.diaryapp.controllers;

import com.technophiles.diaryapp.controllers.reponses.ApiResponse;
import com.technophiles.diaryapp.dtos.UserDto;
import com.technophiles.diaryapp.exceptions.DiaryApplicationException;
import com.technophiles.diaryapp.models.Diary;
import com.technophiles.diaryapp.models.User;
import com.technophiles.diaryapp.services.DiaryService;
import com.technophiles.diaryapp.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/v3/diaryApp/diaries")
@Slf4j
public class DiaryController {
    private DiaryService diaryService;
    private UserService userService;


    @Autowired
    public DiaryController(DiaryService diaryService, UserService userService) {
        this.diaryService = diaryService;
        this.userService = userService;
    }

    @PostMapping("/create/{userId}")
    private ResponseEntity<?> createDiary(@Valid @NotNull @NotBlank @PathVariable("userId") String userId, @NotNull @NotBlank @RequestParam String title){
        log.info("User Service --> {}", userService);
        try {
            User user = userService.findById(Long.valueOf(userId));
            Diary diary = diaryService.createDiary(title,user);
            Diary savedDiary = userService.addDiary(Long.valueOf(userId), diary);
            ApiResponse apiResponse = ApiResponse.builder()
                    .payload(savedDiary)
                    .isSuccessful(true)
                    .message("diary added successfully")
                    .statusCode(201)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

        } catch (DiaryApplicationException e) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .message(e.getMessage())
                    .isSuccessful(false)
                    .statusCode(404)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
