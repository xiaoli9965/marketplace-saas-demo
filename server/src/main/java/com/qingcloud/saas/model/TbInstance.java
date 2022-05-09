package com.qingcloud.saas.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author Alex
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("saas_instance")
@Schema(description = "实例结构体")
public class TbInstance {
    private static final long serialVersionUID = 1L;

    @TableId(value = "instance_id", type = IdType.INPUT)
    private String instanceId;
    private String appId;

    private String spec;
    private String period;
    private String thirdUserId;
    private Long userId;
    private String cloudInfo;

    private LocalDateTime createTime;

    private Boolean status;

    private Boolean debug;
    private Boolean isDel;

}
