package com.example.developjeans.global.config.aws;

import com.example.developjeans.dto.CommentDto;
import com.example.developjeans.global.config.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @Operation(summary = "이미지 조회 하기", description = "총장님 캐릭터 꾸미기 및 이벤트 페이지에서 이미지 조회 기능입니다.", responses = {

    })
    @GetMapping("/image")
    public BaseResponse<?> getWallPapers(@RequestParam String wallpaperName){
        return new BaseResponse<>(s3Service.getImageList(wallpaperName));
    }

}
