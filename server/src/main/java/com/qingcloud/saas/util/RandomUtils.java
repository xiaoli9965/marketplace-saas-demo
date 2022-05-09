package com.qingcloud.saas.util;

import cn.hutool.core.util.RandomUtil;

/**
 * @author Alex
 */
public class RandomUtils {

    public static String instanceId() {
        return "s-i-" + RandomUtil.randomString(12);
    }


    public static String userAccount() {
        return "user-" + RandomUtil.randomString(5);
    }

    public static String userPassword() {
        return "123456";
    }
}
