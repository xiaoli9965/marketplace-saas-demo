package com.qingcloud.saas.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("saas_third_app")
@Schema(hidden = true, description = "第三方app结构体")
public class TbThirdApp {
    private static final long serialVersionUID = 1L;

    @TableId(value = "app_id", type = IdType.INPUT)
    private String appId;

    private String appName;

    private String appKey;
    private String ssoKey;
    private Boolean mockRetry;

}
