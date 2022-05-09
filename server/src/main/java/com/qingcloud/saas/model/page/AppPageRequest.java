package com.qingcloud.saas.model.page;

import com.qingcloud.saas.model.common.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Alex
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AppPageRequest extends BasePageRequest {
    @Schema(required = false, description = "(可以不传)")
    private String appId;
    @Schema(required = false, description = "(可以不传)")
    private String appKey;
}
