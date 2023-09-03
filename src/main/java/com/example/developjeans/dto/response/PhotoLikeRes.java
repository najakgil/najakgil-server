package com.example.developjeans.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoLikeRes {
    private Long photo_id;
    private Integer likes_cnt;
    private String message;
}
