package com.example.developjeans.dto;

import com.example.developjeans.entity.Photo;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class PhotoDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PhotoResponseDto{

        private Long id;

        private Long userId;

        private String imgUrl;

        private Integer likes;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PhotoSaveRequestDto{
        private Long userId;
        private MultipartFile image;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PhotoSaveResponseDto{
        private Long photoId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PhotoGetDto{
        private Long photo_id;
        private String imgUrl;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PhotoLikeDto{
        private Long photo_id;
        private Integer likes_cnt;
        private String message;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PhotoChartDto{
        private Long photo_id;
        private String imgUrl;
        private Integer likes;
    }



 }
