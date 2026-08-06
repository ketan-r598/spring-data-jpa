package io.java_core.taskmanagementapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestingController {

    @GetMapping("/test1")
    public String getTestString() {
        return "<h1>Testing spring web1<h1>";
    }
}
