package com.qingcloud.saas.model.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Alex
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RetData<T> extends Ret {
    /**
     * 返回数据
     */
    @Schema(description = "响应数据",title = "响应数据")
    private T data;
}
