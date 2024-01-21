package com.example.developjeans.entity;

import com.example.developjeans.global.entity.BaseEntity;
import com.example.developjeans.global.entity.Status;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import static com.example.developjeans.global.entity.Status.A;


@Entity
@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhotoLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHOTO_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Photo photo;

    @Enumerated(EnumType.STRING)
    private Status status;

    public PhotoLike(Photo photo){
        this.photo = photo;
        this.status = A;
    }

}
