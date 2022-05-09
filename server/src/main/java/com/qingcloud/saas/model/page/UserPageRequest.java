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
@Schema
public class UserPageRequest extends BasePageRequest {
    @Schema(title = "实例ID(可以不传)",required = false,nullable = true)
    private String instance;
}
