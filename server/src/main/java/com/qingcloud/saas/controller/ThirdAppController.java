package com.qingcloud.saas.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qingcloud.saas.model.TbInstance;
import com.qingcloud.saas.model.TbThirdApp;
import com.qingcloud.saas.model.common.Ret;
import com.qingcloud.saas.model.page.AppPageRequest;
import com.qingcloud.saas.service.IInstanceService;
import com.qingcloud.saas.service.IThirdAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author Alex
 */
@Tag(name = "第三方应用接口")
@Slf4j
@RestController
@RequestMapping("thirdApp")
public class ThirdAppController {

    @Autowired
    private IThirdAppService service;

    @Autowired
    private IInstanceService insService;

    @Operation(summary = "分页查询", description = "仅支持参数 appId,appKey")
    @PostMapping("list")
    public Ret listPage(@RequestBody AppPageRequest pageRequest) {
        LambdaQueryWrapper<TbThirdApp> wp = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(pageRequest.getAppId())) {
            wp.eq(TbThirdApp::getAppId, StrUtil.trim(pageRequest.getAppId()));
        }
        if (StrUtil.isNotBlank(pageRequest.getAppKey())) {
            wp.eq(TbThirdApp::getAppKey, StrUtil.trim(pageRequest.getAppKey()));
        }

        return Ret.page(service.page(pageRequest.buildPage(), wp));
    }

    @Operation(summary = "删除第三方App")
    @DeleteMapping("{id}")
    public Ret del(@PathVariable("id") String id) {
        boolean b = service.removeById(id);
        if (b) {
            return Ret.ok();
        }
        return Ret.err();
    }

    @Operation(summary = "根据实例ID获取App信息")
    @GetMapping("{id}")
    public Ret getApp(@PathVariable("id") String id) {
        TbInstance ins = insService.getById(id);
        if (ins == null) {
            return Ret.err("实例不存在1");
        }
        TbThirdApp app = service.getById(ins.getAppId());
        if (app == null) {
            return Ret.err("实例不存在2");
        }

        HashMap<String, Object> ret = new HashMap<>();
        ret.put("instance", ins);
        ret.put("app", app);
        return Ret.ok(ret);
    }

    @Operation(summary = "添加")
    @PostMapping
    public Ret add(@RequestBody TbThirdApp app) {
        app.setAppId(app.getAppId().trim());
        app.setAppKey(app.getAppKey().trim());
        app.setAppName(app.getAppName().trim());
        app.setSsoKey(app.getSsoKey());
        app.setMockRetry(app.getMockRetry());
        TbThirdApp appInfo = service.getById(app.getAppId());
        if (appInfo != null) {
            appInfo.setAppKey(app.getAppKey().trim());
            appInfo.setAppName(app.getAppName().trim());
            appInfo.setSsoKey(app.getSsoKey());
            appInfo.setMockRetry(app.getMockRetry());
            if (service.updateById(appInfo)) {
                return Ret.ok();
            } else {
                return Ret.err();
            }
        }

        if (service.save(app)) {
            return Ret.ok();
        } else {
            return Ret.err();
        }
    }

    @Operation(summary = "修改")
    @PatchMapping
    public Ret update(@RequestBody TbThirdApp app) {
        if (service.updateById(app)) {
            return Ret.ok();
        } else {
            return Ret.err();
        }
    }


}
