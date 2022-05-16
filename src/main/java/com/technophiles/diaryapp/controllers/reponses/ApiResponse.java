package com.technophiles.diaryapp.controllers.reponses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ApiResponse {
    private Object payload;
    private boolean isSuccessful;
    private int statusCode;
    private String message;
}
