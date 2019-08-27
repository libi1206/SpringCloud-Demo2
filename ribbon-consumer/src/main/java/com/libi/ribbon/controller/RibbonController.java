package com.libi.ribbon.controller;

import com.libi.ribbon.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author :Libi
 * @version :1.0
 * @date :2019-08-27 15:15
 */
@RestController
public class RibbonController {
    @Autowired
    private HelloService helloService;

    @GetMapping("/ribbon-hello")
    public String hello() {
        return helloService.hello();
    }
}
