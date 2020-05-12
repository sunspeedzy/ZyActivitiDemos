package com.imooc.activiti.springboot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyan_g
 */
@RestController
public class HomeController {

    @RequestMapping("/home")
    public String home() {
        return "Hello world!";
    }
}
