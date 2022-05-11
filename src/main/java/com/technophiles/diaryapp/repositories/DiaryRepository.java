package com.technophiles.diaryapp.repositories;

import com.technophiles.diaryapp.models.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
