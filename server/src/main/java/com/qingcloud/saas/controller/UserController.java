package com.qingcloud.saas.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qingcloud.saas.model.TbUser;
import com.qingcloud.saas.model.common.Ret;
import com.qingcloud.saas.model.page.UserPageRequest;
import com.qingcloud.saas.service.IUserService;
import com.qingcloud.saas.util.DevUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alex
 */
@Tag(name = "用户接口")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Operation(summary = "分页查询", description = "仅支持参数 instance")
    @PostMapping("list")
    public Ret listPage(@RequestBody UserPageRequest pageRequest) {
        String instanceSpace = DevUtils.getInstanceSpace();
        LambdaQueryWrapper<TbUser> wp = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(pageRequest.getInstance())) {
            wp.eq(TbUser::getInstance, StrUtil.trim(pageRequest.getInstance()));
        }

        if (StrUtil.isNotBlank(DevUtils.getInstanceSpace())) {
            wp.eq(TbUser::getInstance, DevUtils.getInstanceSpace());
        }

        return Ret.page(userService.page(pageRequest.buildPage(), wp));
    }

}
