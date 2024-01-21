package com.example.developjeans.controller;

import com.example.developjeans.dto.JoinDto;
import com.example.developjeans.dto.KaKaoUserInfo;
import com.example.developjeans.dto.response.LoginDtoRes;
import com.example.developjeans.entity.User;
import com.example.developjeans.global.config.response.BaseResponse;
import com.example.developjeans.repository.UserRepository;
import com.example.developjeans.service.OauthService;
import com.example.developjeans.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "kakao", description = "카카오 로그인/회원가입")
@Slf4j
public class OauthController {

    private final OauthService oauthService;
    private final UserService userService;
    private final UserRepository userRepository;


    @Operation(summary = "카카오 로그인하기", description = "카카오 인가코드 활용")
    @GetMapping("/api/v1/kakao")
    public BaseResponse<?> kakaoCallback(@RequestParam String code)  {
        log.info("code : " + code);
        String accessToken = oauthService.getKakaoAccessToken(code);

        // 카카오 사용자 정보를 가져온다.
        KaKaoUserInfo kaKaoUserInfo = oauthService.getKaKaoUserInfo(accessToken);

        // 카카오 사용자의 고유 ID를 기반으로 데이터베이스에서 회원 정보를 확인
        User existingUser = userRepository.findByKakaoId(kaKaoUserInfo.getId());

        if (existingUser != null) {
            // 이미 가입한 사용자인 경우, 로그인 처리를 수행.
            Long userId = existingUser.getId();
            String jwt = userService.getUserJwt(userId);
            // 클라이언트에게 JWT 토큰 및 사용자 ID를 반환
            return new BaseResponse<>(new LoginDtoRes(jwt, userId, "로그인 성공!"));
        } else {
            // 신규 사용자인 경우, 회원가입 진행
            JoinDto joinDto = new JoinDto();
            joinDto.setKakaoToken(accessToken); // Kakao API로부터 받은 코드를 사용

            try {
                userService.createUser(kaKaoUserInfo, joinDto);
                // 회원가입 성공 시 클라이언트에게 jwt랑 USER_ID 반환.
                return new BaseResponse<String>("회원가입 성공!");
            }
            catch (Exception e) {
                // 회원가입 실패 시 에러 응답을 반환
                return new BaseResponse<>(e.getMessage());

            }
        }
    }




//    @ResponseBody
//    @GetMapping("/api/oauth/kakao")
//    public void kakaoCalllback(@RequestParam String code) {
//        log.info("code : " + code);
//        oauthService.getKakaoAccessToken(code);
//
//    }

//    @PostMapping("/sign-up")
//    public BaseResponse<String> kakaoSignUp(@RequestBody JoinDto joinDto) throws BaseException {
//        KaKaoUserInfo kaKaoUserInfo = oauthService.getKaKaoUserInfo(joinDto.getKakaoToken());
//        if(userRepository.existsByKakaoId(kaKaoUserInfo.getId())){
//            return new BaseResponse<>(BaseResponseStatus.POST_USERS_EXISTS_EMAIL);
//        }
//        userService.createUser(kaKaoUserInfo, joinDto);
//        return new BaseResponse<>("회원가입 성공");
//    }
//
//    @PostMapping("/login")
//    public BaseResponse<LoginDtoRes> kakaoLogin(@RequestBody LoginDto loginDto){
//        KaKaoUserInfo kaKaoUserInfo = oauthService.getKaKaoUserInfo(loginDto.getKakaoToken());
//        if(!userRepository.existsByKakaoId(kaKaoUserInfo.getId())){
//            return new BaseResponse<>(BaseResponseStatus.POST_USERS_NO_EXISTS_USER);
//        }
//
//        Long id = userRepository.findByKakaoId(kaKaoUserInfo.getId()).getId();
//        String jwt = userService.getUserJwt(id);
//        return new BaseResponse<>(new LoginDtoRes(jwt, id));
//    }

//    @GetMapping("/redirect/oauth2/sign-up")
//    public String redirectSignUp(){
//        return "회원가입 성공!";
//    }
//
//    @GetMapping("/login")
//    public String login() {
//
//        return "로그인 성공!";
//    }


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
