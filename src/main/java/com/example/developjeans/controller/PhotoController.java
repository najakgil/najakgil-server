package com.example.developjeans.controller;

import com.example.developjeans.dto.request.SavePhotoReq;
import com.example.developjeans.dto.response.GetPhotoRes;
import com.example.developjeans.dto.response.SavePhotoRes;
import com.example.developjeans.entity.Photo;
import com.example.developjeans.global.config.Response.BaseException;
import com.example.developjeans.global.config.Response.BaseResponse;
import com.example.developjeans.service.PhotoService;
import com.example.developjeans.service.S3Service;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/photo")
@Api(tags = "사진")
public class PhotoController {

    @Autowired
    private PhotoService photoService;


    @ApiOperation("사진 저장 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image", dataTypeClass = Integer.class, paramType = "formData", value = "image")
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            //@ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            //@ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            //@ApiResponse(code = 2004, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 2005, message = "이미지파일이 아닙니다")
    })
    @PostMapping("/upload")
    public BaseResponse<SavePhotoRes> uploadFile(@RequestParam("image") MultipartFile image) {
        SavePhotoRes savePhotoRes = photoService.uploadFile(image);
        //String result = photoService.uploadFile(image);
        //model.addAttribute("result", result);
        return new BaseResponse<>(savePhotoRes);
        //return "upload_file";
    }

    @ApiOperation("사진 조회 API")
    @ApiImplicitParams({

    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2005, message = "이미지파일이 아닙니다")
    })
    @GetMapping
    public BaseResponse<List<GetPhotoRes>> getAllPhoto(){
        List<GetPhotoRes> photoList = photoService.getAllImages();
        return new BaseResponse<>(photoList);
        //public List<Photo> getAllImages() {
        //return photoService.getAllImages();
    }
    @ApiOperation("좋아요 API")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "photoId", paramType = "path", value = "사진 인덱스", example = "1", dataTypeClass = Long.class)
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            //@ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            //@ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            //@ApiResponse(code = 2004, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 2005, message = "이미지파일이 아닙니다"),
            @ApiResponse(code = 2007, message = "존재하지 않는 사진입니다.")
    })
    @PostMapping("/like/{photoId}/{userId}")
    public BaseResponse<String> postLike(@PathVariable Long photoId, @PathVariable Long userId) throws ChangeSetPersister.NotFoundException {
        return new BaseResponse<>(photoService.likePhoto(photoId, userId));

    }



}
