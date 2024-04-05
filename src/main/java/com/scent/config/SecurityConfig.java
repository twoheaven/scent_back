package com.scent.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;

import com.scent.jwt.JwtAccessDeniedHandler;
import com.scent.jwt.JwtAuthenticationEntryPoint;
import com.scent.jwt.TokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

		private final TokenProvider tokenProvider;
	    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	    @Autowired
	    public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
	        this.tokenProvider = tokenProvider;
	        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
	        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
	    }
	    
	    // PasswordEncoder는 BCryptPasswordEncoder를 사용
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
		
	    @SuppressWarnings("removal")
		@Bean
	    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
	        httpSecurity
	                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
	                .csrf().disable()

	                .exceptionHandling()
	                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
	                .accessDeniedHandler(jwtAccessDeniedHandler)

	                // enable h2-console
	                .and()
	                .headers()
	                .frameOptions()
	                .sameOrigin()
	                
	                // CORS 허용 설정 추가
	                .and()
	                .cors(request -> {
	                    CorsConfiguration corsConfiguration = new CorsConfiguration();
	                    corsConfiguration.addAllowedOrigin("http://localhost:3000"); // 출처 허용
	                    corsConfiguration.addAllowedOrigin("https://scenter.co.kr"); // 출처 허용
	                    corsConfiguration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
	                    corsConfiguration.addAllowedHeader("*"); // 모든 헤더 허용
	                    return;
	                }) // CORS 설정을 활성화
	                .authorizeHttpRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

	                
	                // 세션을 사용하지 않기 때문에 STATELESS로 설정
	                .and()
	                .sessionManagement()
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

	                .and()
	                .authorizeHttpRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다.
	                //기본 api
	                .requestMatchers(new AntPathRequestMatcher("/api/authenticate")).permitAll()//로그인 api
	                .requestMatchers(new AntPathRequestMatcher("/api/signup")).permitAll()//회원가입 api
	                .requestMatchers(new AntPathRequestMatcher("/api/send-email")).permitAll()//이메일 api
	                //get api
	                .requestMatchers(new AntPathRequestMatcher("/api/texts/get/**")).permitAll()
	                .requestMatchers(new AntPathRequestMatcher("/api/texts/get")).permitAll()
	                .requestMatchers(new AntPathRequestMatcher("/api/banners/get/**")).permitAll()
	                .requestMatchers(new AntPathRequestMatcher("/api/banners/get")).permitAll()
	                .requestMatchers(new AntPathRequestMatcher("/api/recommends/get/**")).permitAll()
	                .requestMatchers(new AntPathRequestMatcher("/api/recommends/get")).permitAll()
	                .requestMatchers(new AntPathRequestMatcher("/api/recruits/get/**")).permitAll()
	                .requestMatchers(new AntPathRequestMatcher("/api/recruits/get")).permitAll()
	                .requestMatchers(new AntPathRequestMatcher("/api/fields/get/**")).permitAll()
	                .requestMatchers(new AntPathRequestMatcher("/api/fields/get")).permitAll()
	                .requestMatchers(PathRequest.toH2Console()).permitAll()// h2-console 요청 인증 무시
	                .anyRequest().authenticated()
	                
	                .and()
	                .apply(new JwtSecurityConfig(tokenProvider)); // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig class 적용

	        return httpSecurity.build();
	    }
	    
} 