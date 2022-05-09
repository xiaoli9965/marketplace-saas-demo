package com.qingcloud.saas.model.common.qing;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Alex
 */
@Accessors(chain = true)
@Data
public class QingAppInfo {

    private QingInstanceInfo frontEnd;
    private QingInstanceInfo admin;
    private String authUrl;
}
