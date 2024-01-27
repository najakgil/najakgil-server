package com.example.developjeans.global.config.security;

import com.example.developjeans.global.config.security.jwt2.JwtFilter;
import com.example.developjeans.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


//@EnableWebMvc //모든 API에 인증 필요
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {


    private final UserService userService;
    @Value("${jwt.secret}")
    private String secretKey;

    private static final String[] PERMIT_ALL_PATTERNS = new String[] {
            "/v3/api-docs/**",
            "/configuration/**",
            "/swagger*/**",
            "/webjars/**",
            "/swagger-ui/**",
            "/docs",
            "/api/v1/kakao",
            "/photo/chart"
    };

    private static final String[] DO_NOT_PERMIT_PATTERNS = new String[] {
            "/api/v1/users/withdrawal",
            "/api/v1/photo/upload",
            "/api/v1/photo",
            "/api/v1/photo/likes"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception{
        http

                .formLogin(FormLoginConfigurer::disable) // FormLogin 사용 X
                .httpBasic(HttpBasicConfigurer::disable) // httpBasic 사용 X
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )


                //== URL별 권한 관리 옵션 ==//
                // 인가 규칙 설정
                .authorizeHttpRequests(request -> request

                        .requestMatchers(new MvcRequestMatcher(introspector, "/api/v1/kakao")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/swagger-ui/**")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/v3/api-docs/**")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/api/v1/photo/chart")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/api/v1/categories/**")).permitAll()
                        // 나머지는 시큐리티 적용
                        .anyRequest().authenticated()

                )

                // 세션 사용하지 않으므로 STATELESS로 설정
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 토큰에 담긴 정보로 사용자 구분하기
                .addFilterBefore(new JwtFilter(secretKey), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new UsernamePasswordAuthenticationFilter(), LogoutFilter.class);


        http
                // 시큐리티에서 로그아웃 처리. 따로 api 필요 x
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        //.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .addLogoutHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession(false);
                            if (session != null) {
                                session.invalidate();
                            }
                        })
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.sendRedirect("/");
                        })
                        .deleteCookies("remember-me")
                );


        return http.build();



    }

    // 스프링 시큐리티를 통한 암호화 진행
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().
                requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(new AntPathRequestMatcher( "/favicon.ico"))
                .requestMatchers(new AntPathRequestMatcher( "/css/**"))
                .requestMatchers(new AntPathRequestMatcher( "/js/**"))
                .requestMatchers(new AntPathRequestMatcher( "/img/**"))
                .requestMatchers(new AntPathRequestMatcher( "/lib/**"));
    }


}
