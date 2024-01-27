package com.example.developjeans.controller;
import com.example.developjeans.exception.BusinessLogicException;
import com.example.developjeans.exception.ExceptionCode;
import com.example.developjeans.service.PhotoService;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/photo")
@Slf4j
@Tag(name = "photo", description = "사진 관련 기능")
@RequiredArgsConstructor
public class PhotoController {


    private final PhotoService photoService;

    @Operation(summary = "사진 저장하기(다운로드x)", description = "디비에 유저가 만든 사진을 저장합니다.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("userId") Long userId, @RequestPart(value = "image", required = false) MultipartFile image) {

        return ResponseEntity.ok("photoId=" + photoService.uploadFile(image, userId));

    }

    @Operation(summary = "사진 조회하기", description = "마이페이지에서 사진 조회 기능입니다.")
    @GetMapping()
    public ResponseEntity<?> getAllPhoto(@RequestParam("userId") Long userId){

        return ResponseEntity.ok("photoId=" + photoService.getAllImages(userId));


    }

    @Operation(summary = "좋아요", description = "사진에 좋아요를 누를 수 있는 기능입니다.")
    @PostMapping("/likes")
    public ResponseEntity<?> postLike(@RequestParam("photoId") Long photoId, @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(photoService.likePhoto(photoId, userId));
//        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
//        String user = principal.getName();
//
//        Long id = Long.parseLong(user);
//
//        try{
//            if (!id.equals(userId)) {
//                return new BaseResponse<>(INVALID_JWT);
//            }
//            return new BaseResponse<>(photoService.likePhoto(photoId, userId));
//        } catch(Exception e){
//            e.printStackTrace();
//            return new BaseResponse<>(DATABASE_ERROR);
//        }


    }
    @Operation(summary = "사진 차트", description = "홈에 있는 사진 차트 기능입니다.")
    @GetMapping("/chart")
    public ResponseEntity<?> getChartLikes(@RequestParam String standard,
                                           @RequestParam(name = "size") int size){

        return ResponseEntity.ok(photoService.getChart(standard, size));

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
