package com.example.thesimpleeventapp.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class ProjectConfiguration {
    static {
        Dotenv dotenv = Dotenv.configure()
                .directory(".")
                .filename(".env")
                .load();

        System.setProperty("postgres.username",
                Objects.requireNonNull(dotenv.get("POSTGRES_USERNAME")));
        System.setProperty("postgres.password",
                Objects.requireNonNull(dotenv.get("POSTGRES_PASSWORD")));

        System.setProperty("mail.username",
                Objects.requireNonNull(dotenv.get("MAIL_USERNAME")));
        System.setProperty("mail.password",
                Objects.requireNonNull(dotenv.get("MAIL_PASSWORD")));
    }
}
