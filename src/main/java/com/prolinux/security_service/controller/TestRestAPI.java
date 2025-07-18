package com.prolinux.security_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestRestAPI {

    @GetMapping
    Map<String, Object> dataTest(){
        return Map.of("message", "Data Test");
    }



}
