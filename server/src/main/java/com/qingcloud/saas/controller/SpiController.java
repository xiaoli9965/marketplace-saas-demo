package com.qingcloud.saas.controller;

import com.qingcloud.saas.annotation.SpiLog;
import com.qingcloud.saas.common.Constants;
import com.qingcloud.saas.model.common.QingResp;
import com.qingcloud.saas.model.common.qing.QingRequest;
import com.qingcloud.saas.service.ISpiService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SPI接口")
@RestController
public class SpiController {
    @Autowired
    private ISpiService service;

    @Operation(summary = "SPI调用",
            externalDocs = @ExternalDocumentation(description = "内部文档", url = "https://cwiki.yunify.com/pages/viewpage.action?pageId=66172327#Saas服务商接入指南-4.3创建实例")
    )
    @Parameters({
            @Parameter(name = "action", in = ParameterIn.QUERY, required = true,
                    schema = @Schema(type = "string", allowableValues = {
                            Constants.ACTION_CREATE,
                            Constants.ACTION_RENEW,
                            Constants.ACTION_UPGRADE,
                            Constants.ACTION_TEST,
                            Constants.ACTION_EXPIRE,
                            Constants.ACTION_DELETE})
            ),
            @Parameter(name = "user_id", description = "spi 动作", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class)),
            @Parameter(name = "spec", in = ParameterIn.QUERY, required = false, schema = @Schema(implementation = String.class),
                    description = "[CreateAppInstance专属] 定价规格名称 （base64编码）"),
            @Parameter(name = "spec_package", in = ParameterIn.QUERY, required = false, schema = @Schema(implementation = String.class),
                    description = "[CreateAppInstance专属] 定价规格套餐 （base64编码））"),
            @Parameter(name = "period", in = ParameterIn.QUERY, required = false, schema = @Schema(implementation = String.class),
                    description = "[CreateAppInstance专属] 定价-套餐有效期(参数组成： 值_时间单位)"),
            @Parameter(name = "cloud_info", in = ParameterIn.QUERY, required = false, schema = @Schema(implementation = String.class),
                    description = "[CreateAppInstance专属] 当前云环境访问域名, 接入方后续对接时会用到,json字符串格式，通过base64编码传输"),
            @Parameter(name = "instance_id", in = ParameterIn.QUERY, required = false, schema = @Schema(implementation = String.class),
                    description = "实例ID"),
            @Parameter(name = "debug", in = ParameterIn.QUERY, required = false, schema = @Schema(type = "int", defaultValue = "0", allowableValues = {"0", "1"}),
                    description = "如果该值为 1, 则代表当前实例用于调试。 不会进行实际的计费操作"),
            @Parameter(name = "signature", in = ParameterIn.QUERY, required = true, schema = @Schema(implementation = String.class),
                    description = "签名"),
            @Parameter(name = "timestamp", in = ParameterIn.QUERY, required = true, schema = @Schema(implementation = String.class),
                    description = "时间戳，格式：ISO8601 (base64编码)"),
    })
    @SpiLog
    @RequestMapping(method = RequestMethod.GET, value = {"spi","spi1","spi2","spi3"})
    public ResponseEntity<QingResp> qingSpi(@Parameter(hidden = true) QingRequest req) {
        // 业务逻辑
        switch (req.getAction()) {
            case Constants.ACTION_CREATE:
                return service.create(req);
            case Constants.ACTION_RENEW:
                return service.renew(req);
            case Constants.ACTION_UPGRADE:
                return service.upgrade(req);
            case Constants.ACTION_EXPIRE:
                return service.expire(req);
            case Constants.ACTION_DELETE:
                return service.delete(req);
            case Constants.ACTION_TEST:
                return new ResponseEntity<>(QingResp.ok(), HttpStatus.OK);
            default:
                return new ResponseEntity<>(QingResp.error("未知的action"), HttpStatus.BAD_REQUEST);
        }
    }
}
