package com.example.developjeans.controller;

import com.example.developjeans.global.config.response.BaseException;
import com.example.developjeans.global.config.response.BaseResponse;
import com.example.developjeans.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.example.developjeans.global.config.response.BaseResponseStatus.INVALID_JWT;

@RestController
@Slf4j
@Tag(name = "user", description = "회원 관련 기능")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 탈퇴")
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
