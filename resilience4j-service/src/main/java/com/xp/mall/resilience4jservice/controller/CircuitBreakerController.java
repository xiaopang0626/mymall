package com.xp.mall.resilience4jservice.controller;

import com.xp.mall.mallcommon.api.CommonResult;
import com.xp.mall.resilience4jservice.service.CircuitBreakerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/circuitBreaker")
public class CircuitBreakerController {

    private final CircuitBreakerService service;

    @GetMapping("/slow")
    public String slow() {
        return service.slow();
    }

    @GetMapping("/mayFail")
    public String mayFail(int number) {
        return service.mayFail(number);
    }

    @GetMapping("/hello")
    public CommonResult hello() {
        return service.hello();
    }

    @GetMapping("/hello2")
    public String hello2() {
        return service.hello2();
    }
}