package com.example.developjeans.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class KaKaoUserInfo {
    private Long id;
    private String email;
    private String nickName;
}
