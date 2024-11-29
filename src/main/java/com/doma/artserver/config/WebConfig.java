package com.doma.artserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 경로에 대해 CORS 설정
                        .allowedOriginPatterns("*") // 허용할 오리진 설정
//                        .allowedOriginPatterns("https://art-window.com") // 허용할 오리진 설정
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 메서드 설정
                        .allowedHeaders("*") // 허용할 헤더 설정
                        .allowCredentials(false) // 인증 정보 허용 여부
                ;
            }
        };
    }
}

