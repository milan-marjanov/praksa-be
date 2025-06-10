package com.example.thesimpleeventapp.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

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

    public static void configureEnvironmentVariables() {
        Dotenv dotenv = Dotenv.configure()
                .directory("theSimpleEventApp")
                .filename(".env")
                .load();
        System.setProperty("postgres.username", Objects.requireNonNull(dotenv.get("POSTGRES_USERNAME")));
        System.setProperty("postgres.password", Objects.requireNonNull(dotenv.get("POSTGRES_PASSWORD")));

        System.setProperty("mail.username", Objects.requireNonNull(dotenv.get("MAIL_USERNAME")));
        System.setProperty("mail.password", Objects.requireNonNull(dotenv.get("MAIL_PASSWORD")));
    }
}
