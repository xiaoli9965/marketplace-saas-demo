package com.qingcloud.saas.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingcloud.saas.mapper.IInstanceMapper;
import com.qingcloud.saas.mapper.IUserMapper;
import com.qingcloud.saas.model.TbInstance;
import com.qingcloud.saas.model.TbUser;
import com.qingcloud.saas.model.common.Ret;
import com.qingcloud.saas.model.common.qing.QingCreateVo;
import com.qingcloud.saas.model.vo.LoginRequest;
import com.qingcloud.saas.service.IUserService;
import com.qingcloud.saas.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author Alex
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<IUserMapper, TbUser> implements IUserService {

    @Resource
    private IInstanceMapper instanceMapper;

    @Override
    public HashMap<String, Object> login( LoginRequest login) {
        LambdaQueryWrapper<TbUser> wp = Wrappers.lambdaQuery();
        wp.eq(TbUser::getAccount, login.getUsername());
        wp.eq(TbUser::getPassword, login.getPassword());

        HashMap<String, Object> map = new HashMap<>();

        TbUser tbUser = baseMapper.selectOne(wp);
        if (tbUser == null) {
            map.put("status", "error");
            return map;
        }
        map.put("currentAuthority", "admin");
        map.put("status", "ok");
        map.put("userId", tbUser.getId());
        map.put("type", "account");
        map.put("instance", tbUser.getInstance());
        return map;
    }


    @Override
    public TbUser createUser(String insId, boolean isAdmin) {
        String account = RandomUtils.userAccount();
        String password = RandomUtils.userPassword();

        String role = isAdmin ? "console_admin" : "user";
        String username = isAdmin ? "管理员" + RandomUtil.randomString(3) : "普通用户:" + RandomUtil.randomString(3);

        TbUser user = new TbUser()
                .setUsername(username)
                .setInstance(insId)
                .setPassword(password)
                .setAccount(account)
                .setRole(role);

        if (!save(user)) {
            log.error("创建用户失败 {}", user);
            return null;
        }
        return user;
    }

    @Override
    public boolean deleteUsers(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public Ret userAuthLogin(String instanceId, String thirdUserId) {
        LambdaQueryWrapper<TbInstance> wp = Wrappers.lambdaQuery();
        wp.eq(TbInstance::getThirdUserId, thirdUserId).eq(TbInstance::getInstanceId, instanceId);

        TbInstance tbInstance = instanceMapper.selectOne(wp);
        if (tbInstance == null) {
            return Ret.err("userId instanceId 校验失败");
        }

        Long userId = tbInstance.getUserId();
        TbUser tbUser = baseMapper.selectById(userId);
        if (tbUser == null) {
            return Ret.err("校验失败,无效的请求");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("userInfo", tbUser);
        map.put("instanceInfo", tbInstance);
        return Ret.ok(map);
    }
}
