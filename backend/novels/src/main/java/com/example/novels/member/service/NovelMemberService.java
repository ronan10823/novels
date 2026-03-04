package com.example.novels.member.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.novels.member.dto.MemberDTO;
import com.example.novels.member.dto.RegisterDTO;
import com.example.novels.member.entity.Member;
import com.example.novels.member.entity.constant.MemberRole;
import com.example.novels.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Setter
@ToString
@Log4j2
public class NovelMemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void register(RegisterDTO dto) throws IllegalStateException {

        // 중복 이메일 확인
        Optional<Member> result = memberRepository.findById(dto.getEmail());
        if (result.isPresent()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

        Member member = Member.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .fromSocial(false)
                .pw(passwordEncoder.encode(dto.getPassword()))
                .build();
        member.addRole(MemberRole.MEMBER);
        memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("novel service username {}", username);

        // orElseThrow() => NoSuchElementException
        Member member = memberRepository.findByEmailAndFromSocial(username, false)
                .orElseThrow(() -> new UsernameNotFoundException("이메일 확인")); // Supplier<? extends X> exceptionSupplier
        // member => MemberDTO
        // [user,manager,admin]
        log.info("service {}", member);

        MemberDTO dto = new MemberDTO(member.getEmail(),
                member.getPw(), member.getNickname(), member.isFromSocial(),
                member.getRoles().stream().map(role -> role.name()).collect(Collectors.toList()));

        // dto.setNickname(member.getNickname());
        log.info("정신차려 {}", dto);

        return dto;
    }

}
