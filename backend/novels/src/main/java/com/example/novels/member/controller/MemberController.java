package com.example.novels.member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.novels.member.dto.RegisterDTO;
import com.example.novels.member.service.NovelMemberService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/api/member")
@Log4j2
@RestController
public class MemberController {

    @Autowired
    private NovelMemberService novelMemberService;

    @PostMapping("/register")
    public void postRegister(@Valid @RequestBody RegisterDTO registerDTO) {

        novelMemberService.register(registerDTO);
    }

}
