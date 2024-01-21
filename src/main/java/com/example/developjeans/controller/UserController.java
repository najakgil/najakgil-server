package com.example.developjeans.controller;

import com.example.developjeans.global.config.Response.BaseException;
import com.example.developjeans.global.config.Response.BaseResponse;
import com.example.developjeans.service.UserService;
//import io.swagger.annotations.*;
import com.fasterxml.jackson.databind.ser.Serializers;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.example.developjeans.global.config.Response.BaseResponseStatus.INVALID_JWT;

@RestController
@Slf4j
@Api(tags = "User")
//@Tag(name = "User", description = "회원 기능")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @GetMapping("/{userId}")
//    public BaseResponse<?> getUser(@PathVariable Long userId){
//        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
//        String user = principal.getName();
//
//        Long id = Long.parseLong(user);
//
//        try{
//            if (!id.equals(userId)) {
//                return new BaseResponse<>(INVALID_JWT);
//            }
//        } catch (BaseException e){
//            return new BaseResponse<>(e.getStatus());
//        }
//    }


    @ApiOperation("회원 탈퇴 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", dataTypeClass = String.class, paramType = "header", value = "서비스 자체 jwt 토큰")
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다.", response = String.class),
            @ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "존재하지 않는 유저입니다.")
    })
    //@Operation(summary = "탈퇴하기", description = "회원 탈퇴 기능입니다.")
    @DeleteMapping("/withdrawal")
    public BaseResponse<String> deleteUser(@RequestParam("userId") Long userId){
        log.info("탈퇴 api 실행");
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        String user = principal.getName();

        Long id = Long.parseLong(user);

        try{
            if (!id.equals(userId)) {
                return new BaseResponse<>(INVALID_JWT);
            }
            userService.deleteUser(userId);
            return new BaseResponse<>("회원 탈퇴 성공");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }



}
