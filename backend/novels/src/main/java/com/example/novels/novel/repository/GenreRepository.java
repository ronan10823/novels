package com.example.novels.novel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.novels.novel.entity.Genre;
import com.example.novels.novel.entity.Grade;
import com.example.novels.novel.entity.Novel;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
