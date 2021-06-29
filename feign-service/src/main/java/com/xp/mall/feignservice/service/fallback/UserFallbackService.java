package com.xp.mall.feignservice.service.fallback;

import com.xp.mall.feignservice.service.UserService;
import com.xp.mall.mallcommon.api.CommonResult;
import com.xp.mall.mallmodel.model.User;

public class UserFallbackService implements UserService {
    @Override
    public CommonResult<User> getUser(Long id) {
        User defaultUser = new User(-1L, "defaultUser", "123456","123456@qq.com");
        return CommonResult.success(defaultUser);
    }
}
