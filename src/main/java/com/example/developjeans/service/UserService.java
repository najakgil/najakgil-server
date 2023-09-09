package com.example.developjeans.service;

import com.example.developjeans.dto.JoinDto;
import com.example.developjeans.dto.KaKaoUserInfo;
import com.example.developjeans.entity.User;
//import com.example.developjeans.global.config.security.jwt2.JwtUtil;
import com.example.developjeans.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    //private final JwtUtil jwtUtil;

    public void createUser(KaKaoUserInfo kaKaoUserInfo){

        User user = User.builder()
                .email(kaKaoUserInfo.getEmail())
                //.kakaoId(kaKaoUserInfo.getId())
                .nickName(kaKaoUserInfo.getNickName())
                .build();
        userRepository.save(user);
    }



    //private Long expiredMs = 1000 * 60 * 60;
     /*
    public String login(String userName, String password){
        // 인증 과정 생략
        return JwtUtil.createJwt(userName, secretKey, expiredMs);
    } */
}
