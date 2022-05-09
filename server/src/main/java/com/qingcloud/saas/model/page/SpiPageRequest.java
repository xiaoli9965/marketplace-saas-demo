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
public class SpiPageRequest extends BasePageRequest {
    @Schema(title = "spi action(可以不传)",required = false)
    private String action;
    @Schema(title = "主键ID(可以不传)",required = false)
    private String primaryId;
    private String appId;
    @Schema(title = "实例ID(可以不传)",required = false)
    private String instanceId;
}
