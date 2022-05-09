package com.qingcloud.saas.model.common.qing;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Alex
 */
@Accessors(chain = true)
@Data
public class QingInstanceInfo {

    /**
     * 访问地址
     */
    private String url;
    /**
     * 分配的账号
     */
    private String username;
    /**
     * 分配的密码
     */
    private String password;


}
