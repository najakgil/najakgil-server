package com.example.developjeans.controller;

import com.example.developjeans.global.config.Response.BaseException;
import com.example.developjeans.global.config.Response.BaseResponse;
import com.example.developjeans.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.example.developjeans.global.config.Response.BaseResponseStatus.INVALID_JWT;

@RestController
@Slf4j
@Api(tags = "user")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @ApiOperation("회원 탈퇴 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", dataTypeClass = String.class, paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "userId", dataTypeClass = Long.class, paramType = "path", value = "유저 인덱스", example = "1"),
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다.", response = String.class),
            @ApiResponse(code = 2034, message = "존재하지 않는 회원입니다."),
    })
    @DeleteMapping("/{userId}")
    public BaseResponse<String> deleteUser(@PathVariable Long userId){
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
