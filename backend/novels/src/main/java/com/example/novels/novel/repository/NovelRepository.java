package com.example.novels.novel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.novels.novel.entity.Novel;

public interface NovelRepository extends JpaRepository<Novel, Long>, SearchNovelRepository {

}
