package com.qingcloud.saas.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Alex
 */
@Schema(hidden = true, description = "登录用户信息结构体")
@Data
public class UserInfoVO {
    private String access;
    private String avatar = "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png";
    private String name;
    private String phone;
    private String instance;
    private Long userid;
}
