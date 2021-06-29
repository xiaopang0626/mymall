package com.xp.mall.userservice.controller;

import com.xp.mall.mallcommon.api.CommonResult;
import com.xp.mall.mallmodel.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/{id}")
    public CommonResult<User> getUser(@PathVariable Long id) {
        User user = new User();
        LOGGER.info("根据id获取用户信息，用户名称为：{}",user.getName());
        return CommonResult.success(user);
    }

}
