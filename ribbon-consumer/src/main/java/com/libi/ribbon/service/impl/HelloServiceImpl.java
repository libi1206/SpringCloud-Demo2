package com.libi.ribbon.service.impl;

import com.libi.ribbon.service.HelloService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author :Libi
 * @version :1.0
 * @date :2019-08-27 19:46
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Autowired
    private RestTemplate restTemplate;

    private static String SERVICE_NAME = "HELLO-PROVIDER";

    @Override
    @HystrixCommand(fallbackMethod = "helloFailBack")
    public String hello() {
        //填写地址的时候需要使用服务在eureka上填写的服务名称进行调用
        return restTemplate.getForEntity("http://" + SERVICE_NAME + "/hello", String.class).getBody();
    }

    @Override
    public String helloFailBack() {
        return "服务运行错误";
    }
}
