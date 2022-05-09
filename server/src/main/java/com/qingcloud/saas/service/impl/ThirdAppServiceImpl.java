package com.qingcloud.saas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingcloud.saas.mapper.IThirdAppMapper;
import com.qingcloud.saas.model.TbThirdApp;
import com.qingcloud.saas.service.IThirdAppService;
import org.springframework.stereotype.Service;

/**
 * @author Alex
 */
@Service
public class ThirdAppServiceImpl extends ServiceImpl<IThirdAppMapper, TbThirdApp> implements IThirdAppService {
}
