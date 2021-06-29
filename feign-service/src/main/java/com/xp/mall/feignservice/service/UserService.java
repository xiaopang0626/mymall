package com.xp.mall.feignservice.service;

import com.xp.mall.feignservice.service.fallback.UserFallbackService;
import com.xp.mall.mallcommon.api.CommonResult;
import com.xp.mall.mallmodel.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "user-service", fallback = UserFallbackService.class)
public interface UserService {
    @GetMapping("/user/{id}")
    CommonResult<User> getUser(@PathVariable Long id);
}