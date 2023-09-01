package com.example.developjeans.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetChartRes {
    private Long photo_id;
    private String imgUrl;
    private Integer likes;
}
