package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class InfoController {
    
    @GetMapping("/api/info")
    public Map<String, Object> getAppInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("appName", "Spring Boot Demo Application");
        info.put("version", "1.0.0");
        info.put("description", "A simple user management REST API");
        info.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        info.put("javaVersion", System.getProperty("java.version"));
        return info;
    }
}