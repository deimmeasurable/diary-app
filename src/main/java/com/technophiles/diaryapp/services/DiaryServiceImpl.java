package com.technophiles.diaryapp.services;

import com.technophiles.diaryapp.models.Diary;
import com.technophiles.diaryapp.models.User;
import com.technophiles.diaryapp.repositories.DiaryRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class DiaryServiceImpl implements DiaryService{
    @Autowired
    DiaryRepository diaryRepository;

    @Override
    public Diary createDiary(String title, User user) {
        Diary diary = new Diary(title);
        diary.setUser(user);
        return diaryRepository.save(diary);
    }


}
