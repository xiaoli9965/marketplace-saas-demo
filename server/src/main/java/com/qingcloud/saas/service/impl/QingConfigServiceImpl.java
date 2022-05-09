package com.qingcloud.saas.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.qingcloud.saas.common.Constants;
import com.qingcloud.saas.model.TbThirdApp;
import com.qingcloud.saas.service.IInstanceService;
import com.qingcloud.saas.service.IQingConfigService;
import com.qingcloud.saas.service.IThirdAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Alex
 */
@Slf4j
@Service
public class QingConfigServiceImpl implements IQingConfigService {
    @Autowired
    private IThirdAppService appService;
    @Autowired
    private IInstanceService instanceService;

    private String matchAppKey(HttpServletRequest req) {
        String action = req.getParameter("action");
        String appId = req.getParameter("app_id");
        String instanceId = req.getParameter("instance_id");

        log.info("Spi request [{}] app: [{}] , instance: [{}]", action, appId, instanceId);

        if (StrUtil.isNotBlank(appId)) {
            TbThirdApp byId = appService.getById(appId);
            if (byId != null) {
                return byId.getAppKey();
            }
        }

        if (StrUtil.isNotBlank(instanceId)) {
            String appKey = instanceService.getAppKey(instanceId);
            if (StrUtil.isNotBlank(appKey)) {
                return appKey;
            }
        }
        return null;
    }

    @Override
    public Boolean checkSignature(String reqSignature, HttpServletRequest req) {
        if (reqSignature == null || "".equals(reqSignature)) {
            return false;
        }

        String appKey = matchAppKey(req);
        if (StrUtil.isBlank(appKey)) {
            return false;
        }

        Map<String, String[]> parameterMap = req.getParameterMap();

        // 排序参数名, 字典顺序
        List<String> paramsNames = parameterMap.keySet()
                .stream()
                .filter(k -> !Constants.QING_REQ_SIGNATURE_KEY.equals(k))
                .sorted()
                .collect(Collectors.toList());

        ArrayList<String> pairs = new ArrayList<>();
        for (String name : paramsNames) {
            for (String val : parameterMap.get(name)) {
                try {
                    pairs.add(name + "=" + URLEncoder.encode(val, Constants.URL_ENCODE));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        // 拼接签名串
        String toSign = req.getMethod() + "\n" + CollectionUtil.join(pairs, "&");
        HMac mac = new HMac(HmacAlgorithm.HmacSHA256, appKey.getBytes());
        String encodeSignature = Base64.encode(mac.digest(toSign));

        return encodeSignature.equals(reqSignature);
    }

}


