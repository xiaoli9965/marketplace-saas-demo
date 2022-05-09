package com.qingcloud.saas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingcloud.saas.model.TbInstance;
import com.qingcloud.saas.model.TbUser;
import com.qingcloud.saas.model.common.Ret;
import com.qingcloud.saas.model.vo.LoginRequest;

import java.util.HashMap;
import java.util.List;

/**
 * @author Alex
 */
public interface IUserService extends IService<TbUser> {
    HashMap<String, Object> login(LoginRequest login);


    TbUser createUser(String insId, boolean isAdmin);

    boolean deleteUsers(List<Long> ids);

    Ret userAuthLogin(String instanceId, String userId);

}
