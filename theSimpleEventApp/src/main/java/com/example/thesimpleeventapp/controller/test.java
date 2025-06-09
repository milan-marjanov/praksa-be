package com.example.thesimpleeventapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/test")
public class test {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/hello1234")
    public String hello() {
        return "Hello world";
    }
}
