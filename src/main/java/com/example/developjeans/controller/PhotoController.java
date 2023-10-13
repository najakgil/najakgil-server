package com.example.developjeans.controller;

import com.example.developjeans.dto.request.SavePhotoReq;
import com.example.developjeans.dto.response.GetChartRes;
import com.example.developjeans.dto.response.GetPhotoRes;
import com.example.developjeans.dto.response.PhotoLikeRes;
import com.example.developjeans.dto.response.SavePhotoRes;
import com.example.developjeans.entity.Photo;
//import com.example.developjeans.entity.User;
import com.example.developjeans.global.config.Response.BaseException;
import com.example.developjeans.global.config.Response.BaseResponse;
import com.example.developjeans.global.config.Response.BaseResponseStatus;
import com.example.developjeans.service.PhotoService;
import com.example.developjeans.service.S3Service;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.developjeans.global.config.Response.BaseResponseStatus.DATABASE_ERROR;
import static com.example.developjeans.global.config.Response.BaseResponseStatus.INVALID_JWT;

@RestController
@RequestMapping("/photo")
@Api(tags = "photo")
@RequiredArgsConstructor
public class PhotoController {


    private final PhotoService photoService;




    @ApiOperation("사진 저장 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image", dataTypeClass = Integer.class, paramType = "formData", value = "image"),
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class)
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 2005, message = "이미지파일이 아닙니다")
    })
    @PostMapping("/upload/{userId}")
    public BaseResponse<SavePhotoRes> uploadFile(@PathVariable Long userId, @RequestPart(value = "image", required = false) MultipartFile image) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        String user = principal.getName();

        Long id = Long.parseLong(user);

        try{
            if (!id.equals(userId)) {
                return new BaseResponse<>(INVALID_JWT);
            }

            SavePhotoRes savePhotoRes = photoService.uploadFile(image, userId);

            return new BaseResponse<>(savePhotoRes);



        } catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }


    }

    @ApiOperation("사진 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class)
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 2005, message = "이미지파일이 아닙니다")
    })
    @GetMapping("/{userId}")
    public BaseResponse<List<GetPhotoRes>> getAllPhoto(@PathVariable Long userId){
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

            List<GetPhotoRes> photoList = photoService.getAllImages(userId);
            return new BaseResponse<>(photoList);

        } catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }




    }
    @ApiOperation("좋아요 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "photoId", paramType = "path", value = "사진 인덱스", example = "1", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class)
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 2005, message = "이미지파일이 아닙니다"),
            @ApiResponse(code = 2007, message = "존재하지 않는 사진입니다.")
    })
    @PostMapping("/like/{photoId}/{userId}")
    public BaseResponse<PhotoLikeRes> postLike(@PathVariable Long photoId, @PathVariable Long userId) {
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

    @ApiOperation("사진 차트 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "standard", dataTypeClass = String.class, paramType = "query", value = "정렬 조건(likes, createdAt)"),
            @ApiImplicitParam(name = "page", dataTypeClass = Integer.class, paramType = "query", value = "페이지",example = "0"),
            @ApiImplicitParam(name = "size", dataTypeClass = Integer.class, paramType = "query", value = "사이즈",example = "20")
            //@ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class)
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            //@ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            //@ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            //@ApiResponse(code = 2004, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 2042, message = "정렬 방식이 잘못되었습니다.")
    })
    @GetMapping("/chart")
    public BaseResponse<Page<GetChartRes>> getChartLikes(
                                                   @RequestParam String standard,
                                                   @RequestParam int page,
                                                   @RequestParam int size){
//        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
//        String user = principal.getName();
//
//        Long id = Long.parseLong(user);
//
//        if (!id.equals(userId)) {
//            return new BaseResponse<>(INVALID_JWT);
//        }

        return new BaseResponse<>(photoService.getChart(standard, page, size));

    }



}
