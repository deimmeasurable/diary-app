package com.technophiles.diaryapp.services;

import com.technophiles.diaryapp.models.Diary;
import com.technophiles.diaryapp.models.User;
import org.springframework.stereotype.Service;

@Service
public class DiaryServiceImpl implements DiaryService{
    @Override
    public Diary createDiary(String title, User user) {
        Diary diary = new Diary(title);
        diary.setUser(user);
        return diary;
    }
}
