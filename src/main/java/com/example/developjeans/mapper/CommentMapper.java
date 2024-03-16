package com.example.developjeans.mapper;

import com.example.developjeans.dto.CommentDto;
import com.example.developjeans.dto.PhotoDto;
import com.example.developjeans.entity.Comment;
import com.example.developjeans.entity.Photo;
import com.example.developjeans.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * DTO와 엔티티 매핑
 */
@Mapper(
        componentModel = "spring", // spring 구성 모델 지시
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, // 의존성 주입
        unmappedTargetPolicy = ReportingPolicy.ERROR // 매핑 중에 대상 객체의 매핑이 되지 않은 속성에 대한 정책 설정
)
public interface CommentMapper {

    /**
     * Entity -> Dto
     */
    CommentDto.CommentResponseDto toResponseDto(Comment comment);

    /**
     * Dto -> Entity
     */
    //Comment toResponseEntity(CommentDto.CommentResponseDto  commentResponseDto);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "content", ignore = true)
    @Mapping(target = "likes_cnt", ignore = true)
    @Mapping(target = "declaration", ignore = true)
    @Mapping(target="createdAt", ignore = true)
    @Mapping(target="updatedAt", ignore = true)
    @Mapping(target="status", ignore = true)
    Comment toRequestEntity(CommentDto.CommentResponseDto commentResponseDto);


}
