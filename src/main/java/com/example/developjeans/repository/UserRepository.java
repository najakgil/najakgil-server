package com.example.developjeans.repository;

import com.example.developjeans.entity.User;
import com.example.developjeans.global.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    Optional<User> findById(Long aLong);


    Optional<User> findByEmail(String email);

    boolean existsByKakaoId(Long kakaoId);
    User findByKakaoId(Long kakaoId);
    //Optional<User> findBySocialTypeAndKakaoId(SocialType socialType, String kakaoId);
    //Optional<User> findByRefreshToken(String refreshToken);
    

}
