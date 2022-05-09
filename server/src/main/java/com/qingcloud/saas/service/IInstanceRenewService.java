package com.qingcloud.saas.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingcloud.saas.model.TbInstanceRenew;
import com.qingcloud.saas.model.page.SpiPageRequest;

/**
 * @author Alex
 */
public interface IInstanceRenewService extends IService<TbInstanceRenew> {

    Page<TbInstanceRenew> listPage(SpiPageRequest pageRequest);


}
