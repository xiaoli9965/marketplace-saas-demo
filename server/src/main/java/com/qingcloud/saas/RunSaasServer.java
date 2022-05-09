package com.qingcloud.saas;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author Alex
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@MapperScan("com.qingcloud.saas.mapper")
public class RunSaasServer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RunSaasServer.class, args);

        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        String uiPath = env.getProperty("springdoc.swagger-ui.path");
        if (StrUtil.isEmpty(path)) {
            path = "";
        }

        log.info(StrUtil.format("👉 http://localhost:{}{} ", port, path));
        log.info(StrUtil.format("👉 http://localhost:{}{}{}", port, path, uiPath));
    }

}
