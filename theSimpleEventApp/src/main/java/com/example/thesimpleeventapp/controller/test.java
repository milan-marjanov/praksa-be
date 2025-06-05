package com.example.thesimpleeventapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class test {


    @GetMapping("/hello1234")
    public String hello() {
        return "Hello world";
    }
}
