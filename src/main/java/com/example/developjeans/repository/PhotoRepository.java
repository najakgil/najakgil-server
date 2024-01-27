package com.example.developjeans.repository;


import com.example.developjeans.dto.response.GetPhotoRes;
import com.example.developjeans.entity.Photo;
import com.example.developjeans.service.PhotoService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Override
    List<Photo> findAll(Sort sort);

    List<Photo> findByUserId(Long userId);

//    @Query("select new com.example.developjeans.dto.response.GetChartRes(p.id, p.imgUrl, p.likes) from Photo p ")
//    Page<GetChartRes> findAllByOrderByLikesDesc(Pageable pageable);

    Page<Photo> findAllByOrderByCreatedAtDesc(Long lastPageId, PageRequest pageRequest);

//    Page<Photo> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Slice<Photo> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Slice<Photo> findAllByOrderByLikesDesc(Pageable pageable);

    void deleteAllByUserId(Long userId);
}
