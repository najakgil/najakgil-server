//package com.example.developjeans.global.config.security.jwt2;
//
//import io.jsonwebtoken.*;
//import lombok.extern.slf4j.Slf4j;
//
//
//import java.time.Duration;
//import java.util.Date;
//
////@Getter
////@RequiredArgsConstructor
//@Slf4j
////@Service
//public class JwtUtil {
//
//    private static final Long accessTokenValidTime = Duration.ofDays(14).toMillis(); //만료시간 2주
//    private static final Long refreshTokenValidTime = Duration.ofDays(14).toMillis();
//
//    // 회원 정보 조회
//    public static Long getUserId(String token, String secretKey){
//        return Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody()
//                .get("userId", Long.class);
//    }
//
//
//    // token 유효 및 만료 확인
//    public static boolean isExpired(String token, String secretKey){
//        Claims claims = Jwts.parser()
//                .setSigningKey(secretKey.getBytes())
//                .parseClaimsJws(token)
//                .getBody();
//        return false;
//    }
//
//    //refresh 토큰 확인
//    public static boolean isRefreshToken(String token, String secretKey){
//        Header header = Jwts.parser()
//                .setSigningKey(secretKey.getBytes())
//                .parseClaimsJws(token)
//                .getHeader();
//        if(header.get("type").toString().equals("refresh")){
//            return true;
//        }
//        return false;
//    }
//
//    //access 토큰 확인
//    public static boolean isAccessToken(String token, String secretKey){
//        Header header = Jwts.parser()
//                .setSigningKey(secretKey.getBytes())
//                .parseClaimsJws(token)
//                .getHeader();
//        if(header.get("type").toString().equals("access")){
//            return true;
//        }
//        return false;
//    }
//
//    //access 토큰 생성
//    public static String createdAccessToken(Long userId, String secretKey){
//        return createJwt(userId, secretKey, "access", accessTokenValidTime);
//    }
//
//    //refresh 토큰 생성
//    public static String createdRefreshToken(Long userId, String secretKey){
//        return createJwt(userId, secretKey, "refresh", refreshTokenValidTime);
//    }
//
//    public static String createJwt(Long userId, String secretKey, String type, Long tokenValidTime){
//        Claims claims = Jwts.claims();
//        claims.put("userId", userId);
//
//        return Jwts.builder()
//                .setHeaderParam("type", type)
//                .setClaims(claims)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//
//    }
//}
