package com.example.developjeans.global.config.security;

import com.example.developjeans.global.config.security.jwt2.JwtFilter;
import com.example.developjeans.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpSession;


@EnableWebMvc //모든 API에 인증 필요
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {


    private final UserService userService;
    @Value("${jwt.secret}")
    private String secretKey;

    private static final String[] PERMIT_ALL_PATTERNS = new String[] {
            "/v2/api-docs/**",
            "/configuration/**",
            "/swagger*/**",
            "/webjars/**",
            "/swagger-ui/**",
            "/docs",
            "/api/v1/kakao",
            "/h2-console/**",
            "/photo/chart"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http

                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests() //request를 authorize 하겠다는 의미
                .antMatchers(PERMIT_ALL_PATTERNS).permitAll() //Login, join은 언제든지 가능
                .anyRequest().authenticated() //모든 api 인증 요청
                .and()
                .sessionManagement()
                // 세션 사용하지 않으므로 STATELESS로 설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //jwt2 사용하는 경우 씀
                .and()
                .addFilterBefore(new JwtFilter(secretKey), UsernamePasswordAuthenticationFilter.class)

                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
                .build();

                //== URL별 권한 관리 옵션 ==//
                // 인가 규칙 설정
//                .authorizeHttpRequests(authorize -> authorize
//                                .requestMatchers(new AntPathRequestMatcher("/api/v1/kakao")
//                                , new AntPathRequestMatcher("/swagger-ui/**")
//                                , new AntPathRequestMatcher("/v3/api-docs/**")
//                                , new AntPathRequestMatcher("/h2-console/**")
//                                , new AntPathRequestMatcher("/photo/chart")).permitAll()
//                                // 나머지는 시큐리티 적용
//                                .requestMatchers(new AntPathRequestMatcher("/users/{userId}", "DELETE")
//                                , new AntPathRequestMatcher("/photo/**")).authenticated()
//                )
//
//                .formLogin(FormLoginConfigurer::disable) // FormLogin 사용 X
//                .httpBasic(HttpBasicConfigurer::disable) // httpBasic 사용 X
//                .csrf(CsrfConfigurer::disable)
//                .cors(Customizer.withDefaults())
//                .headers(headers -> headers
//                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
//                )

//                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


//        http.logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutUrl("/logout")   // 로그아웃 처리 URL (= form action url)
//                .logoutSuccessUrl("/login") // 로그아웃 성공 후 targetUrl, logoutSuccessHandler 가 있다면 효과 없으므로 주석처리.
//                .addLogoutHandler();
//                .deleteCookies("remember-me"); // 로그아웃 후 삭제할 쿠키 지정
//







    }


}
