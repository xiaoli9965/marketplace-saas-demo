package com.qingcloud.saas.util;

import lombok.SneakyThrows;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author Alex
 */
public class DevUtils {
    @SneakyThrows
    public static void sleep(int m) {
        TimeUnit.SECONDS.sleep(m);
    }

    public static void throwError(Object o) {
        throw new RuntimeException("dev error");
    }


    public static String getInstanceSpace() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        String ins = request.getHeader("saas_instance");
        if (ins == null || "null".equals(ins)) {
            return null;
        }
        return ins;
    }
}
