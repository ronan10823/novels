package com.example.novels.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.novels.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
    @EntityGraph(attributePaths = { "roles" }, type = EntityGraphType.LOAD)
    Optional<Member> findByEmailAndFromSocial(String email, boolean fromSocial);
}
