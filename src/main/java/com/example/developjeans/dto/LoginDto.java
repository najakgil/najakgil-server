package com.example.developjeans.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LoginDto {
    private String kakaoToken;
    private Long user_id;
}
