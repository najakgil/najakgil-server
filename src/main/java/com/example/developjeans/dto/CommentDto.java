package com.example.developjeans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "사진 Response")
public class CommentDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentSaveResponseDto{
        private Long id;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentResponseDto{

        private Long id;

        private String content;

        private LocalDateTime createdAt;

        private Integer likes_cnt;

        private Integer declaration;

    }

}
