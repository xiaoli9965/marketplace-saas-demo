package com.qingcloud.saas.controller;

import com.qingcloud.saas.model.TbInstance;
import com.qingcloud.saas.model.TbUser;
import com.qingcloud.saas.model.common.Ret;
import com.qingcloud.saas.model.common.RetData;
import com.qingcloud.saas.model.vo.LoginRequest;
import com.qingcloud.saas.model.vo.UserInfoVO;
import com.qingcloud.saas.service.IUserService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Tag(name = "登录接口")
@RestController
public class AuthController {

    @Autowired
    protected IUserService userService;

    @Operation(summary = "界面登录")
    @PostMapping("/login/account")
    public HashMap<String, Object> account(@RequestBody LoginRequest login) {
        return userService.login(login);
    }

    @Operation(summary = "获取登录用户信息")
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

    @Data
    class UserAuthLoginDTO {
        @Schema(description = "实例")
        private String instanceId;
        @Schema(description = "用户")
        private String userId;
    }

    @PostMapping("authUrl")
    @Operation(summary = "免密登录")
    public Ret userAuthLogin(@RequestBody UserAuthLoginDTO req) {
        return userService.userAuthLogin(req.getInstanceId(), req.getUserId());
    }

}
