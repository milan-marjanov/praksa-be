package com.example.thesimpleeventapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ProjectConfiguration implements WebMvcConfigurer {

    public ProjectConfiguration() {}

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedOrigins("http://localhost:5173")
                .maxAge(3600)
                .allowCredentials(true);
    }
}
