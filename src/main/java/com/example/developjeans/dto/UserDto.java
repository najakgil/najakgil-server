package com.example.developjeans.dto;

import com.example.developjeans.entity.Photo;
import com.example.developjeans.global.entity.SocialType;
import lombok.*;

import java.util.List;

public class UserDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserRequestDto{
        private String kakaoToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserResponseDto {

        private Long id;

        private String nickName;

        private String password;

        private String email;

        private List<Photo> photoList;

        private Long kakaoId;

        private String refreshToken;

    }
}
