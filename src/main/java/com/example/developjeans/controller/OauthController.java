package com.example.developjeans.controller;

import com.example.developjeans.dto.KaKaoUserInfo;
import com.example.developjeans.dto.LoginDto;
import com.example.developjeans.dto.TokenDto;
import com.example.developjeans.entity.User;
import com.example.developjeans.global.config.Response.BaseResponse;
import com.example.developjeans.global.config.security.jwt.service.JwtService;
import com.example.developjeans.repository.UserRepository;
import com.example.developjeans.service.OauthService;
import com.example.developjeans.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
//@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Slf4j
public class OauthController {

    private final OauthService oauthService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    //private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/login/oauth2/code/oauth2/sign-up")
    public String redirectSignUp(){
        return "회원가입 성공!";
    }

    @GetMapping("/login")
    public String login() {

        return "redirect:/photo/upload";
    }


    //@GetMapping("/login")


//    @ResponseBody
//    @GetMapping("/login/oauth2/code/oauth2/login")
//    public void kakaoCalllback(@RequestParam String code) {
//        log.info("code : " + code);
//
//    }
//    @GetMapping
//    public ResponseEntity<TokenDto> registerFromKakao(@RequestParam(name = "code") String code) {
//        String accessToken = oauthService.getKakaoAccessToken(code);
//    }

//    @PostMapping("/login")
//    public ResponseEntity<String> kakaoCallback(@RequestParam String code) throws IOException {
//        log.info("code : " + code);
//
//        String accessToken = oauthService.getKakaoAccessToken(code);
//
//        if (accessToken.isEmpty()) {
//            return ResponseEntity.badRequest().body("Invalid code");
//        }
//
//        // Kakao 사용자 정보 얻기
//        KaKaoUserInfo userInfo = oauthService.getKaKaoUserInfo(accessToken);
//
//        if (userInfo == null) {
//            return ResponseEntity.badRequest().body("Failed to fetch user info");
//        }
//
//        // 사용자 정보로 회원 가입 또는 로그인 처리
//        User user = userRepository.findByEmail(userInfo.getEmail());
//
//        if(user == null){
//            userService.createUser(userInfo);
//        }
//
//
//        // JWT 토큰 생성
//        String token = jwtService.createAccessToken(user.getEmail());
//
//        return ResponseEntity.ok(token);
//
//
//    }

//    @GetMapping("/login")
//    public BaseResponse<LoginDto> login(@RequestParam(required = false) String code){
//        // 카카오 서버로부터 사용자 정보 받아오기
//        String access_token = oauthService.getKakaoAccessToken(code);
//        KaKaoUserInfo kaKaoUserInfo = oauthService.getKaKaoUserInfo(access_token);
//
//        // 사용자 정보 전달 객체 생성
//        LoginDto loginDto = oauthService;
//
//        return new BaseResponse<>(loginDto);
//    }


}
