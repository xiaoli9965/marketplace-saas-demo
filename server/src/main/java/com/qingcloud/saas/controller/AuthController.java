package com.qingcloud.saas.controller;

import com.qingcloud.saas.model.TbInstance;
import com.qingcloud.saas.model.TbUser;
import com.qingcloud.saas.model.common.Ret;
import com.qingcloud.saas.model.common.RetData;
import com.qingcloud.saas.model.vo.LoginRequest;
import com.qingcloud.saas.model.vo.UserInfoVO;
import com.qingcloud.saas.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class AuthController {

    @Autowired
    protected IUserService userService;

    @PostMapping("/login/account")
    public HashMap<String, Object> inbound(@RequestBody(required = false) LoginRequest login) {
        return userService.login(login);
    }

    @GetMapping("userInfo")
    public UserInfoVO currentUser(@RequestParam Long userId) {
        UserInfoVO info = new UserInfoVO();
        TbUser user = userService.getById(userId);
        info.setAccess(user.getRole());
        info.setUserid(user.getId());
        info.setName(user.getUsername());
        info.setInstance(user.getInstance());
        return info;
    }

    @PostMapping("authUrl")
    public Ret currentUser(@RequestBody HashMap<String, String> req) {
        return userService.userAuthLogin(req.get("instanceId"), req.get("userId"));
    }

}
