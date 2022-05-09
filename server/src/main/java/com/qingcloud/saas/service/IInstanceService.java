package com.qingcloud.saas.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingcloud.saas.model.TbInstance;
import com.qingcloud.saas.model.page.SpiPageRequest;
import com.qingcloud.saas.model.vo.TbInstanceExt;

/**
 * @author Alex
 */
public interface IInstanceService extends IService<TbInstance> {
    String getAppKey(String insId);

    Page<TbInstanceExt> listPage(SpiPageRequest pageRequest);
}
