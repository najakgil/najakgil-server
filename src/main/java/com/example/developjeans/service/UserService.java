package com.example.developjeans.service;

import com.example.developjeans.dto.JoinDto;
import com.example.developjeans.dto.KaKaoUserInfo;
import com.example.developjeans.entity.User;
import com.example.developjeans.global.config.Response.BaseException;
import com.example.developjeans.global.config.Response.BaseResponseStatus;
import com.example.developjeans.global.config.security.jwt2.JwtUtil;
import com.example.developjeans.global.entity.Status;
import com.example.developjeans.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    @Value("${jwt.secret}")
    private String secretKey;
    
    private Long expiredMs = 1000 * 60 * 60l;

    private final UserRepository userRepository;
    
    @Transactional
    public Long createUser(KaKaoUserInfo kaKaoUserInfo, JoinDto joinDto){

        User user = User.builder()
                .email(kaKaoUserInfo.getEmail())
                .kakaoId(kaKaoUserInfo.getId())
                .nickName(kaKaoUserInfo.getNickName())
                .status(Status.A)
                .build();
        userRepository.save(user);

        return user.getId();
    }

    public String getUserJwt(Long userId){
        String jwt = JwtUtil.createdAccessToken(userId, secretKey);
        return jwt;
    }

    @Transactional
    public void deleteUser(Long userId) throws BaseException {

        if(userRepository.existsByIdAndStatus(userId, Status.A)){
            userRepository.deleteById(userId);
        } else{
            throw new BaseException(BaseResponseStatus.POST_USERS_NO_EXISTS_USER);
        }
    }



}
