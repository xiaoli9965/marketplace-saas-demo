package com.qingcloud.saas.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingcloud.saas.mapper.IInstanceMapper;
import com.qingcloud.saas.model.TbInstance;
import com.qingcloud.saas.model.TbInstanceRenew;
import com.qingcloud.saas.model.TbThirdApp;
import com.qingcloud.saas.model.page.SpiPageRequest;
import com.qingcloud.saas.model.vo.TbInstanceExt;
import com.qingcloud.saas.service.IInstanceRenewService;
import com.qingcloud.saas.service.IInstanceService;
import com.qingcloud.saas.service.IThirdAppService;
import com.qingcloud.saas.util.DevUtils;
import com.qingcloud.saas.util.PageBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alex
 */
@Slf4j
@Service
public class InstanceServiceImpl extends ServiceImpl<IInstanceMapper, TbInstance> implements IInstanceService {
    @Autowired
    private IThirdAppService appService;

    @Autowired
    private IInstanceRenewService renewService;

    @Override
    public String getAppKey(String insId) {
        TbInstance instance = baseMapper.selectById(insId);
        if (instance == null) {
            log.error("Get App key failed, instance [{}] is not found", insId);
            return null;
        }
        String appId = instance.getAppId();
        TbThirdApp app = appService.getById(appId);
        if (app == null) {
            log.error("Get App key failed, app [{}] is not found", appId);
            return null;
        }

        if (StrUtil.isBlank(app.getAppKey())) {
            log.error("Get App key failed, third App [{}] is no app key configured", appId);
            return null;
        }
        return app.getAppKey();
    }


    @Override
    public Page<TbInstanceExt> listPage(SpiPageRequest pageRequest) {

        LambdaQueryWrapper<TbInstance> wp = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(pageRequest.getAppId())) {
            wp.eq(TbInstance::getAppId, pageRequest.getAppId());
        }
        if (StrUtil.isNotBlank(pageRequest.getInstanceId())) {
            wp.eq(TbInstance::getInstanceId, pageRequest.getInstanceId());
        }
        if (StrUtil.isNotBlank(DevUtils.getInstanceSpace())) {
            wp.eq(TbInstance::getInstanceId, DevUtils.getInstanceSpace());
        }
        wp.orderByDesc(TbInstance::getCreateTime);
        Page<TbInstance> page = page(pageRequest.buildPage(), wp);

        Page<TbInstanceExt> extPage = PageBeanUtils.copyPage(page, TbInstanceExt.class);
        for (TbInstanceExt record : extPage.getRecords()) {

            LambdaQueryWrapper<TbInstanceRenew> wpr = Wrappers.lambdaQuery();
            wpr.orderByAsc(TbInstanceRenew::getCreateTime);
            wpr.eq(TbInstanceRenew::getInstanceId, record.getInstanceId());
            record.setRenews(renewService.list(wpr));

            TbThirdApp app = appService.getById(record.getAppId());
            record.setAppName(app.getAppName());
        }
        return extPage;

    }
}
