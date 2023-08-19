package com.example.developjeans.global.config.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException {
    private BaseResponseStatus status;
}
