package com.example.developjeans.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/oauth/kakao")
@Slf4j
public class OauthController {

    @GetMapping
    public void kakaoCallback(@RequestParam String code){
        log.info("code : " + code);
    }

}
