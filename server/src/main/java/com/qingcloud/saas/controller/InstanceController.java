package com.qingcloud.saas.controller;

import com.qingcloud.saas.model.common.Ret;
import com.qingcloud.saas.model.page.SpiPageRequest;
import com.qingcloud.saas.service.IInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alex
 */
@Tag(name = "实例查询接口")
@RestController
@RequestMapping("instance")
public class InstanceController {

    @Autowired
    private IInstanceService service;

    @Operation(summary = "分页查询", description = "仅支持参数 action,instanceId")
    @PostMapping("list")
    public Ret listPage(@RequestBody SpiPageRequest pageRequest) {
        return Ret.page(service.listPage(pageRequest));
    }
}
