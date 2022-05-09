package com.qingcloud.saas.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSON;
import com.google.gson.Gson;
import com.qingcloud.saas.mapper.IInstanceMapper;
import com.qingcloud.saas.model.TbInstance;
import com.qingcloud.saas.model.TbInstanceRenew;
import com.qingcloud.saas.model.TbThirdApp;
import com.qingcloud.saas.model.TbUser;
import com.qingcloud.saas.model.common.QingResp;
import com.qingcloud.saas.model.common.qing.QingAppInfo;
import com.qingcloud.saas.model.common.qing.QingCreateVo;
import com.qingcloud.saas.model.common.qing.QingInstanceInfo;
import com.qingcloud.saas.model.common.qing.QingRequest;
import com.qingcloud.saas.service.*;
import com.qingcloud.saas.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Alex
 */
@Slf4j
@Service
public class QingSpiServiceImpl implements ISpiService {


    @Autowired
    private IInstanceService instanceService;
    @Autowired
    private IInstanceRenewService renewService;
    @Autowired
    private IUserService userService;

    @Resource
    private IInstanceMapper iInstanceMapper;

    @Autowired
    private IThirdAppService appService;

    @Value("${saas.host}")
    private String serverHost;

    private ResponseEntity<QingResp> respError() {
        QingCreateVo create = new QingCreateVo();
        create.setSuccess(false);
        return new ResponseEntity<>(create, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QingResp> create(QingRequest req) {
        log.info("用户开通: {}", new Gson().toJson(req));

        String spec = Base64.decodeStr(req.getSpec());
        String cloudInfo = Base64.decodeStr(req.getCloud_info());

        String instanceId = RandomUtils.instanceId();

        TbUser adminUser = userService.createUser(instanceId, true);
        TbUser normalUser = userService.createUser(instanceId, false);
        if (adminUser == null || normalUser == null) {
            log.error("开通实例实例失败, 创建用户失败 app:[{}] userId:[{}]", req.getApp_id(), req.getUser_id());
            return respError();
        }



        TbInstance instance = new TbInstance()
                .setInstanceId(instanceId)
                .setAppId(req.getApp_id())
                .setCloudInfo(cloudInfo)
                .setSpec(spec)
                .setThirdUserId(req.getUser_id())
                .setPeriod(req.getPeriod())
                .setDebug(req.getDebug())
                .setUserId(adminUser.getId())
                .setStatus(true);

        TbThirdApp appInfoMock = appService.getById(instance.getAppId());
        if (appInfoMock != null) {
            if (appInfoMock.getMockRetry() && getRandomBoolean()) {
                log.info("[测试重试机制] 拒绝SPI ");
                return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }
        }

        if (!instanceService.save(instance)) {
            log.error("开通实例实例失败 app:[{}] userId:[{}]", req.getApp_id(), req.getUser_id());
            userService.deleteUsers(Arrays.asList(adminUser.getId(), adminUser.getId()));
            return respError();
        }

        TbInstanceRenew instanceRenew = new TbInstanceRenew()
                .setInstanceId(instance.getInstanceId())
                .setPeriod(req.getPeriod())
                .setSpec(spec)
                .setAction(req.getAction());
        renewService.save(instanceRenew);

        QingCreateVo create = new QingCreateVo();
        create.setSuccess(true);
        create.setInstanceId(instanceId);
        QingAppInfo appInfo = new QingAppInfo();
        appInfo.setFrontEnd(new QingInstanceInfo()
                .setUrl(serverHost + "/user/login")
                .setUsername(normalUser.getAccount())
                .setPassword(normalUser.getPassword())
        );
        appInfo.setAuthUrl(serverHost + "/sso/auth");
        create.setAppInfo(appInfo);
        return new ResponseEntity<>(create, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QingResp> renew(QingRequest req) {
        log.info("用户续费: {}", new Gson().toJson(req));

        String instanceId = req.getInstance_id();

        TbInstance instance = iInstanceMapper.selectById(req.getInstance_id());
        if (instance == null) {
            log.error("实例不存在 [{}]", req.getInstance_id());
            return new ResponseEntity<>(QingResp.error("实例不存在 " + req.getInstance_id()), HttpStatus.OK);
        }

        TbThirdApp appInfo = appService.getById(instance.getAppId());
        if (appInfo != null) {
            if (instance.getDebug() && appInfo.getMockRetry() && getRandomBoolean()) {
                log.info("[测试重试机制] 拒绝SPI ");
                return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }
        }

        String spec = Base64.decodeStr(req.getSpec());
        TbInstanceRenew instanceRenew = new TbInstanceRenew()
                .setInstanceId(instanceId)
                .setPeriod(req.getPeriod())
                .setSpec(spec)
                .setAction(req.getAction());

        renewService.save(instanceRenew);

        TbInstance tbInstance = new TbInstance()
                .setInstanceId(instanceId)
                .setPeriod(req.getPeriod())
                .setStatus(true)
                .setSpec(spec);
        instanceService.updateById(tbInstance);
        return new ResponseEntity<>(QingResp.ok(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QingResp> upgrade(QingRequest req) {
        log.info("用户升级: {}", new Gson().toJson(req));
        String instanceId = req.getInstance_id();

        TbInstance instance = iInstanceMapper.selectById(req.getInstance_id());
        if (instance == null) {
            log.error("实例不存在 [{}]", req.getInstance_id());
            return new ResponseEntity<>(QingResp.error("实例不存在 " + req.getInstance_id()), HttpStatus.OK);
        }

        TbThirdApp appInfo = appService.getById(instance.getAppId());
        if (appInfo != null) {
            if (instance.getDebug() && appInfo.getMockRetry() && getRandomBoolean()) {
                log.info("[测试重试机制] 拒绝SPI ");
                return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }
        }

        String spec = Base64.decodeStr(req.getSpec());
        TbInstanceRenew instanceRenew = new TbInstanceRenew()
                .setInstanceId(instanceId)
                .setPeriod(req.getPeriod())
                .setSpec(spec)
                .setAction(req.getAction());

        TbInstance tbInstance = new TbInstance().setInstanceId(instanceId)
                .setPeriod(req.getPeriod())
                .setStatus(true)
                .setSpec(spec);
        instanceService.updateById(tbInstance);

        renewService.save(instanceRenew);
        return new ResponseEntity<>(QingResp.ok(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QingResp> expire(QingRequest req) {
        log.info("实例过期: {}", new Gson().toJson(req));
        TbInstance instance = iInstanceMapper.selectById(req.getInstance_id());
        if (instance == null) {
            log.error("实例不存在 [{}]", req.getInstance_id());
            return new ResponseEntity<>(QingResp.error("实例不存在 " + req.getInstance_id()), HttpStatus.OK);
        }

        TbThirdApp appInfoMock = appService.getById(instance.getAppId());
        if (appInfoMock != null) {
            if (instance.getDebug() && appInfoMock.getMockRetry() && getRandomBoolean()) {
                log.info("[测试重试机制] 拒绝SPI ");
                return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }
        }

        instance.setStatus(false);
        iInstanceMapper.updateById(instance);

        return new ResponseEntity<>(QingResp.ok(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QingResp> delete(QingRequest req) {
        log.info("实例删除: {}", new Gson().toJson(req));
        TbInstance instance = iInstanceMapper.selectById(req.getInstance_id());
        if (instance == null) {
            log.error("实例不存在 [{}]", req.getInstance_id());
            return new ResponseEntity<>(QingResp.error("实例不存在 " + req.getInstance_id()), HttpStatus.OK);
        }

        TbThirdApp appInfoMock = appService.getById(instance.getAppId());
        if (appInfoMock != null) {
            if (instance.getDebug() && appInfoMock.getMockRetry() && getRandomBoolean()) {
                log.info("[测试重试机制] 拒绝SPI ");
                return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }
        }

        instance.setIsDel(true);
        iInstanceMapper.updateById(instance);

        return new ResponseEntity<>(QingResp.ok(), HttpStatus.OK);
    }

    public static boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextInt(100) < 70;
    }
}
