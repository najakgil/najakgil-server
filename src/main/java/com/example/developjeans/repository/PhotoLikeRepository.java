package com.example.developjeans.repository;

import com.example.developjeans.entity.Photo;
import com.example.developjeans.entity.PhotoLike;
import com.example.developjeans.entity.User;
import com.example.developjeans.global.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoLikeRepository extends JpaRepository<PhotoLike, Long> {


    boolean existsByPhotoAndUserAndStatus(Photo photo, User user, Status status);
}
