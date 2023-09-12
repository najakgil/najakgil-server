package com.example.developjeans.repository;

import com.example.developjeans.dto.response.GetChartRes;
import com.example.developjeans.dto.response.GetPhotoRes;
import com.example.developjeans.entity.Photo;
import com.example.developjeans.service.PhotoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Query("select new com.example.developjeans.dto.response.GetChartRes(p.id, p.imgUrl, p.likes) from Photo p ")
    Page<GetChartRes> findAllByOrderByLikesDesc(Pageable pageable);

    @Query("select new com.example.developjeans.dto.response.GetChartRes(p.id, p.imgUrl, p.likes) from Photo p ")
    Page<GetChartRes> findAllByOrderByCreatedAtDesc(Pageable pageable);


}
