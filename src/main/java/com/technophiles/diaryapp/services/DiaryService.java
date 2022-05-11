package com.technophiles.diaryapp.services;

import com.technophiles.diaryapp.models.Diary;
import com.technophiles.diaryapp.models.User;

public interface DiaryService {
    Diary createDiary(String title, User user);
}
