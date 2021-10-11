package com.spring.tutorial.domain.user.controller;

import com.spring.tutorial.domain.user.dto.AuthDto;
import com.spring.tutorial.domain.user.entity.Authority;
import com.spring.tutorial.domain.user.entity.User;
import com.spring.tutorial.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
//@RequestMapping("/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/join")
    ResponseEntity<Long> user_join(@RequestBody AuthDto.RequestJoin req) {

        User user = new User(
                req.getEmail(),
                bCryptPasswordEncoder.encode(req.getPassword()),
                req.getName(),
                Authority.ROLE_USER);

        userRepository.save(user);

        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    @GetMapping("/test")
    ResponseEntity<String> test_no_selectDb() {

        log.info("test_no_selectDb");
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
