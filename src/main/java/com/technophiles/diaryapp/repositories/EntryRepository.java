package com.technophiles.diaryapp.repositories;

import com.technophiles.diaryapp.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {
}
