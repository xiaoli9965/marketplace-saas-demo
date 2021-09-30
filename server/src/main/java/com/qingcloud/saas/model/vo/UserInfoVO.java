package com.qingcloud.saas.model.vo;

import lombok.Data;

/**
 * @author Alex
 */
@Data
public class UserInfoVO {
    private String access;
    private String avatar = "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png";
    private String name;
    private String phone;
    private String instance;
    private Long userid;
}
