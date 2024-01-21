package com.example.developjeans.mapper;

import com.example.developjeans.dto.UserDto;
import com.example.developjeans.entity.User;
import org.mapstruct.*;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring", // spring 구성 모델 지시
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, // 의존성 주입
        unmappedTargetPolicy = ReportingPolicy.ERROR // 매핑 중에 대상 객체의 매핑이 되지 않은 속성에 대한 정책 설정
)
public interface UserMapper {

    /**
     * Entity -> Dto
     */
    UserDto.UserResponseDto toResponseDto(User user);

    /**
     * Dto -> Entity
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(target="createdAt", ignore = true)
    @Mapping(target="updatedAt", ignore = true)
    @Mapping(target="status", ignore = true)
    User toResponseEntity(UserDto.UserResponseDto userResponseDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target="id", ignore = true)
    @Mapping(target="nickName", ignore = true)
    @Mapping(target="createdAt", ignore = true)
    @Mapping(target="updatedAt", ignore = true)
    @Mapping(target="status", ignore = true)
    @Mapping(target="password", ignore = true)
    @Mapping(target="email", ignore = true)
    @Mapping(target="kakaoId", ignore = true)
    @Mapping(target="refreshToken", ignore = true)
    @Mapping(target = "photoList", ignore = true)
    User toRequestEntity(UserDto.UserRequestDto userRequestDto);
}
