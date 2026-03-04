package com.example.novels.novel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.novels.novel.entity.Grade;
import com.example.novels.novel.entity.Novel;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Modifying
    @Query("DELETE FROM Grade g WHERE g.novel.id = :id")
    void deleteByNovel(Long id);
}
