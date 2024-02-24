package com.example.developjeans.controller;
import com.example.developjeans.dto.PhotoDto;
import com.example.developjeans.exception.BusinessLogicException;
import com.example.developjeans.exception.ExceptionCode;
import com.example.developjeans.global.config.aws.S3Service;
import com.example.developjeans.global.config.response.BaseResponse;
import com.example.developjeans.service.PhotoService;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/photo")
@Slf4j
@Tag(name = "photo", description = "사진 관련 기능")
@RequiredArgsConstructor
public class PhotoController {


    private final PhotoService photoService;

    @Operation(summary = "사진 저장하기(다운로드x)", description = "디비에 유저가 만든 사진을 저장합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "default response", content = @Content(schema = @Schema(implementation = PhotoDto.PhotoSaveResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> uploadFile(@RequestParam("userId") Long userId, @RequestPart(value = "image", required = false) MultipartFile image) {

        return new BaseResponse<>("photoId=" + photoService.uploadFile(image, userId));

    }

    @Operation(summary = "사진 조회하기", description = "마이페이지에서 사진 조회 기능입니다.", responses = {
            @ApiResponse(responseCode = "200", description = "default response", content = @Content(schema = @Schema(implementation = PhotoDto.PhotoResponseDto.class))),
    })
    @GetMapping()
    public BaseResponse<?> getAllPhoto(@RequestParam("userId") Long userId){

        return new BaseResponse<>(photoService.getAllImages(userId));


    }

    @Operation(summary = "좋아요", description = "사진에 좋아요를 누를 수 있는 기능입니다.", responses = {
            @ApiResponse(responseCode = "200", description = "default response", content = @Content(schema = @Schema(implementation = PhotoDto.PhotoLikeDto.class))),
    })
    @PostMapping("/likes")
    public BaseResponse<?> postLike(@RequestParam("photoId") Long photoId, @RequestParam("userId") Long userId) {

        return new BaseResponse<>(photoService.likePhoto(photoId, userId));

    }

    @Operation(summary = "사진 차트", description = "likes: 인기순, latest: 최신순", responses = {
            @ApiResponse(responseCode = "200", description = "default response", content = @Content(schema = @Schema(implementation = PhotoDto.PhotoChartDto.class))),
    })
    @GetMapping("/chart")
    public BaseResponse<?> getChartLikes(@RequestParam String sort,
                                           @RequestParam(name = "size") int size,
                                           @RequestParam Long lastPhotoId){

        return new BaseResponse<>(photoService.getChart(sort, size, lastPhotoId));

    }



    @Operation(summary = "사진 상세 조회", responses = {
            @ApiResponse(responseCode = "200", description = "default response", content = @Content(schema = @Schema(implementation = PhotoDto.PhotoResponseDto.class)))
    })
    @GetMapping("/detail")
    public BaseResponse<?> getPhotoDetail(@RequestParam Long photoId){
        return new BaseResponse<>(photoService.getDetail(photoId));
    }

    @Operation(summary = "사진 다운로드")
    @GetMapping("/download")
    public BaseResponse<?> downloadImage(@RequestParam Long photoId) throws IOException {
        return new BaseResponse<>(photoService.downloadImage(photoId));
    }


//    @Operation(summary = "사진 차트", description = "모든 사진을 모아볼 수 있는 기능입니다.")
//    @GetMapping("/chart")
//    public ResponseEntity<Page<GetChartRes>> getChartLikes(
//                                                   @RequestParam String standard,
//                                                   @RequestParam int page,
//                                                   @RequestParam int size){
//
//        return ResponseEntity.ok(photoService.getChart(standard, page, size));
//
//    }





}
