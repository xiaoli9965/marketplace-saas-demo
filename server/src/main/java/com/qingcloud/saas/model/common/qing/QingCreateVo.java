package com.qingcloud.saas.model.common.qing;

import com.qingcloud.saas.model.common.QingResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Alex
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class QingCreateVo extends QingResp {
    private String instanceId;
    private QingAppInfo appInfo;

}
