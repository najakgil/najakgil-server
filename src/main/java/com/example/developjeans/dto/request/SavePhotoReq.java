package com.example.developjeans.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class SavePhotoReq {
    private Long userId;
    private MultipartFile image;
}
