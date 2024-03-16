package com.example.developjeans.entity;

import com.example.developjeans.global.entity.BaseEntity;
import com.example.developjeans.global.entity.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Integer likes_cnt;

    private Integer declaration;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void like(Comment comment, Integer likes_cnt){
        this.likes_cnt = likes_cnt + 1;
    }

    public void unlike(Comment comment, Integer likes_cnt){
        this.likes_cnt = likes_cnt - 1;
    }

    public void reports(Comment comment, Integer declaration){
        this.declaration = declaration + 1;
    }

    public void unreports(Comment comment, Integer declaration){
        this.declaration = declaration - 1;
    }

}
