package com.xp.mall.resilience4jservice.service;

import com.xp.mall.mallcommon.api.CommonResult;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@CircuitBreaker(name = "backendA")
@Retry(name = "retryBackendA")
@RateLimiter(name = "backendA")
public class CircuitBreakerService {

    private final CircuitBreakerFactory cbFactory;
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service-url.user-service}")
    private String userServiceUrl;

    public CommonResult hello() {
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, 1);
    }

    public String hello2() {
        //创建一个 CircuitBreakerConfig 出来，然后配置一下故障率阈值，等待时间以及环形缓冲区大小等；
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .build();
        //根据第一步创建出来的 CircuitBreakerConfig ，再去创建一个 CircuitBreaker 对象；
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("backendA", circuitBreakerConfig);
        //通过 Try.ofSupplier 方法去发送一个请求，如果请求抛出异常，则在 recover 方法中进行服务降级处理，recover 可以写多个；
        Try<String> supplier = Try.ofSupplier(io.github.resilience4j.circuitbreaker.CircuitBreaker
                .decorateSupplier(circuitBreaker,
                        () -> restTemplate.getForObject(userServiceUrl + "/user/{1}", String.class, 1)))
                .recover(Exception.class, "有异常，访问失败!");
        return supplier.get();
    }

    public String slow() {
        return cbFactory.create("slow")
                .run(this::getString, throwable -> "fallback");
    }

    public String mayFail(int number) {
        return cbFactory.create("default")
                .run(() -> {
                    if (number < 0) {
                        int a = number / 0;
                    }
                    return String.valueOf(number);
                }, throwable -> "fallback");
    }

    private String getString() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "slow";
    }
}