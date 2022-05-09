package com.qingcloud.saas.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qingcloud.saas.model.TbSpiRecord;
import com.qingcloud.saas.model.common.Ret;
import com.qingcloud.saas.model.page.SpiPageRequest;
import com.qingcloud.saas.service.ISpiRecordService;
import com.qingcloud.saas.util.DevUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alex
 */
@Tag(name = "SPI调用记录")
@RestController
@RequestMapping("spiRecord")
public class SpiRecordController {

    @Autowired
    private ISpiRecordService service;

    @Operation(summary = "分页查询", description = "仅支持参数 action,primaryId")
    @PostMapping("list")
    public Ret listPage(@RequestBody SpiPageRequest pageRequest) {
        LambdaQueryWrapper<TbSpiRecord> wp = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(pageRequest.getAction())) {
            wp.eq(TbSpiRecord::getAction, pageRequest.getAction());
        }
        if (StrUtil.isNotBlank(pageRequest.getPrimaryId())) {
            wp.eq(TbSpiRecord::getPrimaryId, StrUtil.trim(pageRequest.getPrimaryId()));
        }

        if (StrUtil.isNotBlank(DevUtils.getInstanceSpace())) {
            wp.eq(TbSpiRecord::getPrimaryId, DevUtils.getInstanceSpace());
        }
        wp.orderByDesc(TbSpiRecord::getCreateTime);
        return Ret.page(service.page(pageRequest.buildPage(), wp));
    }

}
