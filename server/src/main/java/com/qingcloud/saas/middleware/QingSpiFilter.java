package com.qingcloud.saas.middleware;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.qingcloud.saas.common.Constants;
import com.qingcloud.saas.model.common.QingResp;
import com.qingcloud.saas.service.IQingConfigService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Alex
 */
@Slf4j
@WebFilter(urlPatterns = "/spi/*")
@Order(999)
public class QingSpiFilter implements Filter {
    @Autowired
    private IQingConfigService qingConfigService;

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        log.debug("请求[{}], Header:[{}]", req.getRequestURI(), req.getHeader(Constants.QING_HEAD_SIGNATURE_KEY));

        if (!req.getRequestURI().contains("/spi")) {
            chain.doFilter(request, response);
            return;
        }

        if (req.getParameter("action").equals(Constants.ACTION_TEST)) {
            respJson(response, QingResp.ok());
            return;
        }

        String timestamp = req.getParameter("time_stamp");
        String signature = req.getParameter("signature");

        if (StrUtil.isBlank(timestamp)) {
            respJson(response, QingResp.error("Invalid request"));
            return;
        }
        timestamp = URLDecoder.decode(timestamp, Constants.URL_ENCODE);
        LocalDateTime reqDate = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        Duration duration = Duration.between(reqDate, LocalDateTime.now(ZoneOffset.UTC));

        //验证消息有效期
        if (duration.toMillis() > Constants.MESSAGE_VALIDITY_PERIOD) {
            // 如果时间误差大于3秒则认为请求过期,不处理.  对接方可自行选择是否需要处理
            log.error("Message expired: {}", new Gson().toJson(req.getParameterMap()));
            respJson(response, QingResp.error("Message expired"));
            return;
        }

        if (signature == null) {
            signature = req.getHeader(Constants.QING_HEAD_SIGNATURE_KEY);
        }

        // 验证签名
        boolean isOk = qingConfigService.checkSignature(signature, req);
        if (!isOk) {
            log.error("Invalid signature : {}", new Gson().toJson(req.getParameterMap()));
            respJson(response, QingResp.error("Invalid signature"));
            return;
        }
        chain.doFilter(request, response);
    }


    private void respJson(ServletResponse response, Object obj) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(new Gson().toJson(obj));
    }


}
