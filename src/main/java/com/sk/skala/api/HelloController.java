package com.sk.skala.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Hello, DevOps Pipeline!";
    }

    // 간단 헬스엔드포인트 (Actuator와 별개)
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
