package com.qingcloud.saas.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingcloud.saas.mapper.IInstanceRenewMapper;
import com.qingcloud.saas.model.TbInstanceRenew;
import com.qingcloud.saas.model.page.SpiPageRequest;
import com.qingcloud.saas.service.IInstanceRenewService;
import com.qingcloud.saas.util.DevUtils;
import org.springframework.stereotype.Service;

/**
 * @author Alex
 */
@Service
public class InstanceRenewServiceImpl  extends ServiceImpl<IInstanceRenewMapper, TbInstanceRenew> implements IInstanceRenewService {
    @Override
    public Page<TbInstanceRenew> listPage(SpiPageRequest pageRequest) {
        LambdaQueryWrapper<TbInstanceRenew> wp = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(pageRequest.getAction())) {
            wp.eq(TbInstanceRenew::getAction, pageRequest.getAction());
        }

        if (StrUtil.isNotBlank(pageRequest.getInstanceId())) {
            wp.eq(TbInstanceRenew::getInstanceId, pageRequest.getInstanceId());
        }
        if (StrUtil.isNotBlank(DevUtils.getInstanceSpace())) {
            wp.eq(TbInstanceRenew::getInstanceId, DevUtils.getInstanceSpace());
        }
        wp.orderByDesc(TbInstanceRenew::getCreateTime);
        return page(pageRequest.buildPage(), wp);
    }
}
