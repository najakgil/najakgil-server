package com.example.developjeans.global.config.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
     /*
        code는 순차적으로 작성
        2010 ~ 2019 유저 관련 형식적 validation
        2020 ~ 2029 유저 관련 논리적 validation
        2030 ~ 2039 홈 관련 형식적 validation
        2040 ~ 2049 홈 관련 논리적 validation
        등등
     */

    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    NOT_ALLOW_METHOD(false, 405, "잘못된 메소드입니다."),
    INTERNAL_SERVER_ERROR(false, 500, "서버에러 입니다."),

    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false, 2003, "권한이 없는 유저의 접근입니다."),
    INVALID_USER(false, 2004, "존재하지 않는 유저입니다."),
    INVALID_IMAGE_FILE(false, 2005, "이미지파일이 아닙니다."),
    INVALID_KAKAO_TOKEN(false, 2006, "유효한 카카오토큰이 아닙니다."),
    INVALID_PHOTO(false, 2007, "유효하지 않은 사진 파일 형식입니다."),



    POST_USERS_INVALID_EMAIL(false, 2008, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false, 2009, "중복된 이메일입니다."),
    POST_USERS_EXISTS_NICKNAME(false, 2010, "중복된 닉네임입니다."),
    POST_USERS_NO_EXISTS_EMAIL(false, 2011, "존재하지 않는 이메일입니다."),
    POST_USERS_EXISTS(false, 2012, "이미 가입된 회원입니다."),
    POST_USERS_NO_EXISTS_USER(false, 2013, "존재하지 않는 회원입니다."),








    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false, 3014, "없는 아이디거나 비밀번호가 틀렸습니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다.");


    // 5000 : 필요시 만들기



    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
