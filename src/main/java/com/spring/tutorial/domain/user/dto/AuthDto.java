package com.spring.tutorial.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthDto {

    @Getter
    @NoArgsConstructor
    public static class RequestLogin {
        private String email;
        private String password;
    }

    @Getter
    @NoArgsConstructor
    public static class RequestJoin {
        private String email;
        private String password;
        private String name;
    }


}
