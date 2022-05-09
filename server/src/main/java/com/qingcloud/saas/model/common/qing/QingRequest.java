package com.qingcloud.saas.model.common.qing;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Alex
 */
@Data
@Schema(description = "SPI请求体")
public class QingRequest {
    @Schema(description = "spi 动作")
    private String action;

    @Schema(description = "用户唯一标识")
    private String user_id;

    @Schema(description = "应用唯一id")
    private String app_id;

    @Schema(description = " 定价规格名称 （base64编码）")
    private String spec;
    @Schema(description = " 定价规格套餐 （base64编码）")
    private String spec_package;

    @Schema(description = "定价-套餐有效期(参数组成： 值_时间单位)")
    private String period;

    @Schema(description = "当前云环境访问域名, 接入方后续对接时会用到,json字符串格式，通过base64编码传输")
    private String cloud_info;

    @Schema(description = "实例ID")
    private String instance_id;

    @Schema(description = "如果该值为ture, 则代表当前实例用于调试。 不会进行实际的计费操作")
    private Boolean debug;

    @Schema(description = "订单id")
    private String order_id;

    @Schema(description = "签名")
    private String signature;
    @Schema(description = "时间戳，格式：ISO8601 (base64编码)")
    private String timestamp;
}
