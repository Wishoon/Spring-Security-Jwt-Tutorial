package com.spring.tutorial.global.security;

import com.spring.tutorial.domain.user.entity.User;
import com.spring.tutorial.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username)
                .map(this::createCustomUser)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private CustomUser createCustomUser(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getAuthority().toString());

        log.info("CreateCustomUser - 커스텀 유저 클래스 생성");
        return new CustomUser(user.getId(), user.getEmail(), user.getPassword(), Collections.singleton(grantedAuthority));
    }
}
