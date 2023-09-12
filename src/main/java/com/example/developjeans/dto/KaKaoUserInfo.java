package com.example.developjeans.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KaKaoUserInfo {
    private Long id;
    private String email;
    private String nickName;
}
