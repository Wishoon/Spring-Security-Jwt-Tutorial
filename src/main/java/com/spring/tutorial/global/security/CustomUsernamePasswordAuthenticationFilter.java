package com.spring.tutorial.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.tutorial.domain.user.dto.AuthDto;
import com.spring.tutorial.global.jwt.TokenDto;
import com.spring.tutorial.global.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProvider tokenProvder;

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                      TokenProvider tokenProvider) {
        super.setAuthenticationManager(authenticationManager);
        this.tokenProvder = tokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("AttemptAuthentication - 인증 로직 시작 ");
        try {
            AuthDto.RequestLogin credential =
                    new ObjectMapper().readValue(request.getInputStream(), AuthDto.RequestLogin.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credential.getEmail(),
                            credential.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        log.info("Authorities List:" + authResult.getAuthorities());
        TokenDto tokenDto = tokenProvder.generateTokenDto(authResult);

        response.addHeader("token", tokenDto.getAccessToken());
    }


}
