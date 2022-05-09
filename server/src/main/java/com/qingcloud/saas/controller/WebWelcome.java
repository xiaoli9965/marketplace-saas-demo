package com.qingcloud.saas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Alex
 */
@Controller
public class WebWelcome {

    @RequestMapping("/")
    public String welcome() {
        return "index";
    }
}
