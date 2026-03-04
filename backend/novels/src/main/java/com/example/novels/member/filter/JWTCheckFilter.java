package com.example.novels.member.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.novels.member.dto.MemberDTO;
import com.example.novels.member.utils.JWTUtil;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    // jwt token 검사 제외 url 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        // /api/novels + /api/novles/12
        // /swagger-ui
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();
        log.trace("check uri {}", path);

        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")
                || path.startsWith("/api/member/login")
                || path.startsWith("/api/member/register")) {
            return true;
        }

        if ("GET".equals(request.getMethod()) && path.startsWith("/api/novels")) {
            return true;
        }

        // \\d+$ : \d == 숫자 0-9 / + : 1 ~ 무제한 / $ : 끝나는
        // if (path.startsWith("/api/novels") || path.matches("/api/novels/\\d+$")) {
        // return true;
        // }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("----------------- JWT Filter -----------------");

        // 클라이언트는 헤더를 통해서 토큰 서버로 전송
        String authHeaderStr = request.getHeader("Authorization");

        // 토큰 값 시작 문자열 : Bearer
        if (authHeaderStr != null && authHeaderStr.startsWith("Bearer ")) {

            try {
                // "Bearer " 이후의 문자 분리
                String accessToken = authHeaderStr.substring(7);
                // 토큰 유효검증
                Map<String, Object> claims = JWTUtil.validateToken(accessToken);
                log.info("claims {}", claims);

                // claims 값 추출
                String email = (String) claims.get("email");
                String pw = (String) claims.get("pw");
                String nickname = (String) claims.get("nickname");
                Boolean social = (Boolean) claims.get("social");
                List<String> roles = (List<String>) claims.get("roles");

                MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social, roles);

                // 인증정보 => SecurityContext 저장
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        memberDTO,
                        pw, memberDTO.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);

            } catch (Exception e) {
                log.error("JWT token error ");
                log.error(e.getMessage());

                // 에러메세지를 전달(json)
                Gson gson = new Gson();
                String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
                response.setContentType("application/json");
                PrintWriter printWriter = response.getWriter();
                printWriter.println(msg);
                printWriter.close();
            }

        }

    }

}
