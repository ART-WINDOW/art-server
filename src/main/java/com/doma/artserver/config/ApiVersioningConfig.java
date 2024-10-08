package com.doma.artserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {

    private String apiPrefix = "/api/v1";

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // application.yml에서 설정한 경로 프리픽스를 모든 컨트롤러에 적용
        configurer.addPathPrefix(apiPrefix, c -> true);
    }
}
