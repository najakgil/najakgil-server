package com.example.developjeans.entity;

import com.example.developjeans.global.entity.BaseEntity;
import com.example.developjeans.global.entity.Role;
import com.example.developjeans.global.entity.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "kakao_id")
    private Long kakaoId;
    private String email;
    private String nickName;
    private String password; // 비밀번호




    @OneToMany(mappedBy = "user")
    private List<Photo> photoList = new ArrayList<>();



    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }



}
