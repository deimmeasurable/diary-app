package com.technophiles.diaryapp.controllers;


import com.technophiles.diaryapp.controllers.reponses.ApiResponse;
import com.technophiles.diaryapp.exceptions.DiaryApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

@ControllerAdvice
@Slf4j
public class DiaryExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({DiaryApplicationException.class})
    public ResponseEntity<Object> diaryApplicationExceptionHandler(DiaryApplicationException exception, WebRequest request){
        ApiResponse apiResponse = ApiResponse.builder()
                .message(exception.getMessage())
                .isSuccessful(false)
                .statusCode(400)
                .build();
        return handleExceptionInternal(exception, apiResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> exceptionHandler(Exception exception, WebRequest request){
        exception.printStackTrace();
        log.info("Here now now now now ");
        ApiResponse apiResponse = ApiResponse.builder()
                .message(exception.getMessage())
                .isSuccessful(false)
                .statusCode(400)
                .build();
        return handleExceptionInternal(exception, apiResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<Object> sqlExceptionHandler(SQLException exception, WebRequest request){
        log.info("Here now now ");

        ApiResponse apiResponse = ApiResponse.builder()
                .message(exception.getMessage())
                .isSuccessful(false)
                .statusCode(400)
                .build();
        return handleExceptionInternal(exception, apiResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> constraintViolationExceptionHandler(ConstraintViolationException exception, WebRequest request){
        log.info("Here now");

        exception.printStackTrace();
        ApiResponse apiResponse = ApiResponse.builder()
                .message(exception.getMessage())
                .isSuccessful(false)
                .statusCode(400)
                .build();
        return handleExceptionInternal(exception, apiResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
