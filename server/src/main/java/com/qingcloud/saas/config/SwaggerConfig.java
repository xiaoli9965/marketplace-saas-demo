package com.qingcloud.saas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


/**
 * 官方文档
 * https://springdoc.org/faq.html#how-can-i-deploy-the-doploy-springdoc-openapi-ui-behind-a-reverse-proxy.
 *
 * @author Alex
 */
@Slf4j
@Configuration
public class SwaggerConfig {

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public GroupedOpenApi groupOpenApi() {
        String[] packagesToscan = {"com.qingcloud"};
        return GroupedOpenApi.builder().group("saas")
                .packagesToScan(packagesToscan)

                .addOpenApiCustomiser(serverOpenApiCustomiser1())
                .build();
    }

    public OpenApiCustomiser serverOpenApiCustomiser1() {
        Server server = new Server().url("http://localhost:"+serverPort+"/api").description("SaaS");
        List<Server> servers = new ArrayList<>();
        servers.add(server);
        return openApi -> openApi.setServers(servers);
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().title("SaaS API").version("dev").description("青云AppCenter团队 内部测试用")
                        .contact(new Contact().name("内部文档").url("https://cwiki.yunify.com/pages/viewpage.action?pageId=66172327"))
                        .termsOfService("https://www.qingcloud.com/")
                        .license(new License().name("QingCloud Technologies")
                                .url("https://www.qingcloud.com/")));
    }
}
