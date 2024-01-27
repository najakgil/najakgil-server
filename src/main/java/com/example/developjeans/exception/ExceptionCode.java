package com.example.developjeans.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    /**
     * 200 SUCCESSFUL : 정상처리
     */
    SUCCESSFULL(OK, "정상처리 되었습니다"),
    DELETE_SUCCESSFUL(OK, "정상적으로 삭제되었습니다."),


    /**
     * 400 BAD_REQUEST : 잘못된 요청
     */
    EMPTY_JWT(BAD_REQUEST, "JWT를 입력해주세요."),
    INVALID_ACCESS_TOKEN(BAD_REQUEST, "액세스 토큰이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다."),
    INVALID_IMAGE_FILE(BAD_REQUEST, "유효하지 않은 사진 파일 형식입니다."),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰의 유저 정보가 일치하지 않습니다."),
    CREATE_FAIL_USER(BAD_REQUEST, "회원 가입에 실패하였습니다."),
    INVALID_SORT(BAD_REQUEST, "정렬 기준이 잘못되었습니다."),


    /**
     * 401 UNAUTHORIZED : 인증되지 않은 사용자
     */
    INVALID_USER_JWT(UNAUTHORIZED, "권한이 없는 유저의 접근입니다."),
    UNAUTHORIZED_USER(UNAUTHORIZED, "존재하지 않는 계정 정보입니다"),




    /**
     * 404 NOT_FOUND : 해당 Resources 찾을 수 없음
     */
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자 입니다"),
    FILE_IS_NOT_EXIST(NOT_FOUND, "해당 파일이 존재하지 않습니다"),
    IMAGE_IS_NOT_EXIST(NOT_FOUND, "해당 이미지가 존재하지 않습니다"),

    /**
     * 서버 관련 에러
     */
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 에러 입니다."),
    DATABASE_ERROR(INTERNAL_SERVER_ERROR, "데이터베이스 연결에 실패했습니다.")


    ;


    private final HttpStatus httpStatus;
    private final String message;
}
