package com.xp.mall.feignservice.service;

import com.xp.mall.mallcommon.api.CommonResult;
import com.xp.mall.mallmodel.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service", fallback = UserService.UserFallbackService.class)
public interface UserService {
    @GetMapping("/user/{id}")
    CommonResult<User> getUser(@PathVariable Long id);

    @Component
    class UserFallbackService implements UserService {
        @Override
        public CommonResult<User> getUser(Long id) {
            User defaultUser = new User(-1L, "defaultUser", "123456", "123456@qq.com");
            return CommonResult.success(defaultUser);
        }
    }

}