package com.qingcloud.saas.aspectj;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.qingcloud.saas.annotation.SpiLog;
import com.qingcloud.saas.common.Constants;
import com.qingcloud.saas.model.TbSpiRecord;
import com.qingcloud.saas.model.common.qing.QingCreateVo;
import com.qingcloud.saas.service.ISpiRecordService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 操作日志记录处理
 *
 * @author ruoyi
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private ISpiRecordService recordService;

    @Pointcut("@annotation(com.qingcloud.saas.annotation.SpiLog)")
    public void logPointCut() {
    }


    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            SpiLog controllerSpiLog = getAnnotationLog(joinPoint);
            if (controllerSpiLog == null) {
                return;
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            JSONObject jsonObject = JSONUtil.parseObj(jsonResult);
            String primaryId = StrUtil.isNotBlank(request.getParameter("app_id")) ? request.getParameter("app_id") : request.getParameter("instance_id");
            if (request.getParameter("action").equals(Constants.ACTION_CREATE)) {
                Map<String, String> createMap = jsonObject.get("body", Map.class);
                if (createMap != null && StrUtil.isNotBlank(createMap.get("instanceId"))) {
                    primaryId = createMap.get("instanceId");
                }
            }

            TbSpiRecord rc = new TbSpiRecord()
                    .setPrimaryId(primaryId)
                    .setAction(request.getParameter("action"))
                    .setParams(request.getQueryString())
                    .setReq(new Gson().toJson(request.getParameterMap()))
                    .setResp(new Gson().toJson(jsonResult));

            recordService.save(rc);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }


    /**
     * 是否存在注解，如果存在就获取
     */
    private SpiLog getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(SpiLog.class);
        }
        return null;
    }

}
