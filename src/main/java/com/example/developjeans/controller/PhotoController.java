package com.example.developjeans.controller;

import com.example.developjeans.dto.response.GetChartRes;
//import com.example.developjeans.entity.User;
import com.example.developjeans.global.config.response.BaseException;
import com.example.developjeans.global.config.response.BaseResponse;
import com.example.developjeans.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.developjeans.global.config.response.BaseResponseStatus.DATABASE_ERROR;
import static com.example.developjeans.global.config.response.BaseResponseStatus.INVALID_JWT;

@RestController
@RequestMapping("/api/v1/photo")
@Slf4j
@Tag(name = "photo", description = "사진 관련 기능")
@RequiredArgsConstructor
public class PhotoController {


    private final PhotoService photoService;

    @Operation(summary = "사진 저장하기(다운로드x)", description = "디비에 유저가 만든 사진을 저장합니다.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> uploadFile(@RequestParam("userId") Long userId, @RequestPart(value = "image", required = false) MultipartFile image) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        String user = principal.getName();

        Long id = Long.parseLong(user);

        try{
            if (!id.equals(userId)) {
                return new BaseResponse<>(INVALID_JWT);
            }

//            SavePhotoRes savePhotoRes = photoService.uploadFile(image, userId);
            return new BaseResponse<>("photoId: " + photoService.uploadFile(image, userId));


        } catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }

    }

    @Operation(summary = "사진 조회하기", description = "마이페이지에서 사진 조회 기능입니다.")
    @GetMapping()
    public BaseResponse<?> getAllPhoto(@RequestParam("userId") Long userId){
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        String user = principal.getName();

        if (user == null || user.isEmpty()) {
            // 사용자 이름이 null 또는 빈 문자열이면 처리할 수 없으므로 예외 처리 또는 오류 응답을 반환.
            return new BaseResponse<>(INVALID_JWT);
        }


        try{
            Long id = Long.parseLong(user);

            if (!id.equals(userId)) {
                return new BaseResponse<>(INVALID_JWT);
            }

//            List<GetPhotoRes> photoList = photoService.getAllImages(userId);
//            return new BaseResponse<>(photoList);
            return new BaseResponse<>(photoService.getAllImages(userId));

        } catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }


    }

    @Operation(summary = "좋아요", description = "사진에 좋아요를 누를 수 있는 기능입니다.")
    @PostMapping("/likes")
    public BaseResponse<?> postLike(@RequestParam("photoId") Long photoId, @RequestParam("userId") Long userId) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        String user = principal.getName();

        Long id = Long.parseLong(user);

        try{
            if (!id.equals(userId)) {
                return new BaseResponse<>(INVALID_JWT);
            }
            return new BaseResponse<>(photoService.likePhoto(photoId, userId));
        } catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    @Operation(summary = "사진 차트", description = "모든 사진을 모아볼 수 있는 기능입니다.")
    @GetMapping("/chart")
    public BaseResponse<Page<GetChartRes>> getChartLikes(
                                                   @RequestParam String standard,
                                                   @RequestParam int page,
                                                   @RequestParam int size){

        return new BaseResponse<>(photoService.getChart(standard, page, size));

    }

    /**
     * 카테고리별 캐릭터 사진 반환 api
     */

//    @GetMapping("/{categoryId}")
//    public BaseResponse<?> getImageFileList(@PathVariable Long categoryId){
//
//    }




}
