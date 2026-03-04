package com.example.novels.member.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class MemberDTO extends User /* implements OAuth2User */ {

    // member entity 정보 + 인증정보
    private String email;

    private String pw;

    private String nickname;

    private boolean fromSocial;

    private List<String> roles = new ArrayList<>();

    // OAuth2User 가 넘겨주는 attr 담기 위해
    private Map<String, Object> attr;

    public MemberDTO(String username, String pw, String nickname, boolean fromSocial,
            List<String> roles) {
        super(username, pw,
                roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList()));
        this.email = username;
        this.pw = pw;
        this.fromSocial = fromSocial;
        this.nickname = nickname;
        this.roles = roles;
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("email", email);
        dataMap.put("pw", pw);
        dataMap.put("nickname", nickname);
        dataMap.put("social", fromSocial);
        dataMap.put("roles", roles);

        return dataMap;
    }

    // OAuth2User
    // public MemberDTO(String username, String pw, String nickname, boolean
    // fromSocial,
    // Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr)
    // {
    // this(username, pw, nickname, fromSocial, authorities);
    // this.attr = attr;
    // }

    // OAuth2User
    // @Override
    // public Map<String, Object> getAttributes() {
    // return this.attr;
    // }
}
