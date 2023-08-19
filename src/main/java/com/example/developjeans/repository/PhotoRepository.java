package com.example.developjeans.repository;

import com.example.developjeans.entity.Photo;
import com.example.developjeans.service.PhotoService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Override
    List<Photo> findAll(Sort sort);
}
