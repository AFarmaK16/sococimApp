package com.example.demo.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("/WEB-INF/view/frontend/build/static/");
//        registry.addResourceHandler("/*.js")
//                .addResourceLocations("/WEB-INF/view/frontend/build/");
//        registry.addResourceHandler("/*.json")
//                .addResourceLocations("/WEB-INF/view/frontend/build/");
//        registry.addResourceHandler("/*.ico")
//                .addResourceLocations("/WEB-INF/view/frontend/build/");
//        registry.addResourceHandler("/index.html")
//                .addResourceLocations("/WEB-INF/view/frontend/build/index.html");
    }

}
