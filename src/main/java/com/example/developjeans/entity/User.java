package com.example.developjeans.entity;

import com.example.developjeans.global.entity.BaseEntity;
import com.example.developjeans.global.entity.Role;
import com.example.developjeans.global.entity.SocialType;
import com.example.developjeans.global.entity.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;



@Entity
//@Table(name = "\"USER\"")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id")
    private Long kakaoId;
    private String email;
    private String nickName;
    private String password; // 비밀번호

    @Enumerated(EnumType.STRING)
    private Status status;


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Photo> photoList = new ArrayList<>();

    private String refreshToken;



    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }



}
