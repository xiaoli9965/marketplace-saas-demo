package com.qingcloud.saas.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Alex
 */
@Data
@Schema(hidden = true, description = "登录结构体")
public class LoginRequest {

    @Schema(description = "登录账号")
    private String username;
    @Schema(description = "密码")
    private String password;
}
