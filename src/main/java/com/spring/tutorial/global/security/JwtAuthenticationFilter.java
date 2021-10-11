package com.spring.tutorial.global.security;

import com.spring.tutorial.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("DoFilterInternal - 토큰 인증 로직 시작 ");
        String token = tokenProvider.resolveToken(request);

        log.info("TokenValidate - 토큰 인증 분기 시작 ");
        if (token != null && tokenProvider.validateToken(token)) {
            log.info("TokenValidate - 토큰이 있고 인증이 된 경우 ");
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}

/**
 * 1. OncePerRequestFilter 를 상속받은 Custom Filter 를 정의하고 doFilterInternal 메서드를
 * 오버라이드하여 User Authentication 을 구현
 */
