package com.example.developjeans.global.config.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

//    @GetMapping()
//    public BaseResponse<?> getFileList(@RequestParam String categoryName){
//        return new BaseResponse<>(s3Service.getImageList(categoryName));
//    }
}
