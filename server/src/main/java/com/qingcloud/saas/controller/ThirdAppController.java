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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author Alex
 */
@RestController
@RequestMapping("thirdApp")
public class ThirdAppController {

    @Autowired
    private IThirdAppService service;

    @Autowired
    private IInstanceService insService;

    @GetMapping("list")
    public Ret listPage(AppPageRequest pageRequest) {
        LambdaQueryWrapper<TbThirdApp> wp = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(pageRequest.getAppId())) {
            wp.eq(TbThirdApp::getAppId, StrUtil.trim(pageRequest.getAppId()));
        }
        if (StrUtil.isNotBlank(pageRequest.getAppKey())) {
            wp.eq(TbThirdApp::getAppKey, StrUtil.trim(pageRequest.getAppKey()));
        }

        return Ret.page(service.page(pageRequest.buildPage(), wp));
    }

    @DeleteMapping("{id}")
    public Ret del(@PathVariable("id") String id) {
        boolean b = service.removeById(id);
        if (b) {
            return Ret.ok();
        }
        return Ret.err();
    }

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

    @PostMapping
    public Ret add(@RequestBody TbThirdApp app) {
        app.setAppId(app.getAppId().trim());
        app.setAppKey(app.getAppKey().trim());
        app.setAppName(app.getAppName().trim());
        app.setSsoKey(app.getSsoKey().trim());

        if (service.save(app)) {
            return Ret.ok();
        } else {
            return Ret.err();
        }
    }

    @PatchMapping
    public Ret update(@RequestBody TbThirdApp app) {
        if (service.updateById(app)) {
            return Ret.ok();
        } else {
            return Ret.err();
        }
    }


}
