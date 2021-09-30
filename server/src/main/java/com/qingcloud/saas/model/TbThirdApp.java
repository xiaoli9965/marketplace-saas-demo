package com.qingcloud.saas.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class TbThirdApp {
    private static final long serialVersionUID = 1L;

    @TableId(value = "app_id", type = IdType.INPUT)
    private String appId;

    private String appName;

    private String appKey;
    private String ssoKey;


}
