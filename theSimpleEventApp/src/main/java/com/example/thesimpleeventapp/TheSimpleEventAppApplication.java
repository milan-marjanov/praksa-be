package com.example.thesimpleeventapp;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TheSimpleEventAppApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure()
                .directory("theSimpleEventApp")
                .filename(".env")
                .load();
        System.setProperty("postgres.username", dotenv.get("POSTGRES_USERNAME"));
        System.setProperty("postgres.password", dotenv.get("POSTGRES_PASSWORD"));

        System.setProperty("mail.username", dotenv.get("MAIL_USERNAME"));
        System.setProperty("mail.password", dotenv.get("MAIL_PASSWORD"));

        SpringApplication.run(TheSimpleEventAppApplication.class, args);
    }

}
