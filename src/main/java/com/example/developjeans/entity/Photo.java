package com.example.developjeans.entity;

import com.example.developjeans.global.entity.BaseEntity;
import com.example.developjeans.global.entity.Status;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;



import static com.example.developjeans.global.entity.Status.D;


@Entity
@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Photo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "image")
    private String imgUrl;

    //private String status;

    private Integer likes;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void unLikePhoto(Photo photo, Integer likes){
        this.status = D;
        this.likes = likes - 1;
    }



}
