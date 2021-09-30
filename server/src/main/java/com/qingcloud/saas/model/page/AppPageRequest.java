package com.qingcloud.saas.model.page;

import com.qingcloud.saas.model.common.BasePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Alex
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AppPageRequest extends BasePageRequest {

    private String appId;
    private String appKey;
}
