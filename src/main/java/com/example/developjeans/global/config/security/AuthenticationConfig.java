package com.example.developjeans.global.config.security;

import com.example.developjeans.global.config.security.jwt2.JwtFilter;
import com.example.developjeans.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Base64;


@EnableWebMvc //모든 API에 인증 필요
@RequiredArgsConstructor
@Configuration
public class AuthenticationConfig {

    @Autowired
    private final UserService userService;
    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securitryFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests() //request를 authorize 하겠다는 의미
                .antMatchers("/oauth2/authorization/kakao", "/login/**", "/user", "/sign-up", "/oauth2/**", "/api/oauth/kakao").permitAll() //Login, join은 언제든지 가능
                .antMatchers(HttpMethod.POST, "/**").authenticated() //모든 POST 인증 요청
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //jwt2 사용하는 경우 씀
                .and()
                .addFilterBefore(new JwtFilter(secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
