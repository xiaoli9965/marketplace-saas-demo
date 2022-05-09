package com.qingcloud.saas.model.vo;

import com.qingcloud.saas.model.TbInstance;
import com.qingcloud.saas.model.TbInstanceRenew;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Alex
 */
@Schema(hidden = true, description = "实例信息扩展")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbInstanceExt extends TbInstance {
    private List<TbInstanceRenew> renews;
    private String appName;
}
