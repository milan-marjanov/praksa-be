package com.example.thesimpleeventapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.example.thesimpleeventapp.configuration.ProjectConfiguration.configureEnvironmentVariables;

@SpringBootApplication
public class TheSimpleEventAppApplication {

    public static void main(String[] args) {

        configureEnvironmentVariables();

        SpringApplication.run(TheSimpleEventAppApplication.class, args);
    }

}
