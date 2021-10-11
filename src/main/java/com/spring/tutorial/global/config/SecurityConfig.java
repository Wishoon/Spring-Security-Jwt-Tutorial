package com.spring.tutorial.global.config;

import com.spring.tutorial.global.jwt.TokenProvider;
import com.spring.tutorial.global.security.CustomUsernamePasswordAuthenticationFilter;
import com.spring.tutorial.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()

                .and()
                // UsernamePassFilter 대신 Custom 한 JwtAuthenticationFilter 를 생성하여 수행
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/**/join", "/**/login").permitAll()
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    // 커스텀 UsernamePasswordAuthenticationFilter 생성 (attemptAuthentication, success)
    @Bean
    public CustomUsernamePasswordAuthenticationFilter getAuthenticationFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter authenticationFilter =
                new CustomUsernamePasswordAuthenticationFilter(authenticationManager(), tokenProvider);

        return authenticationFilter;
    }
}

/**
 * .addFilterBefore()
 * UsernameAuthenticationFilter 도 실행되지만 JwtAuthenticationFilter 가 먼저 실행되기 때문에 정상 수행시 그냥 거치고 넘어가짐.
 */