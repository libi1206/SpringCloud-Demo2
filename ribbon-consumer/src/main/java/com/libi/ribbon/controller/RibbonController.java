package com.libi.ribbon.controller;

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
    private RestTemplate restTemplate;

    private static String SERVICE_NAME = "HELLO-PROVIDER";

    @GetMapping("/ribbon-hello")
    public String hello() {
        //填写地址的时候需要使用服务在eureka上填写的服务名称进行调用
        return restTemplate.getForEntity("http://" + SERVICE_NAME + "/hello", String.class).getBody();
    }
}
