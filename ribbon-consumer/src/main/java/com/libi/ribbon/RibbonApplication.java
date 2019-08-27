package com.libi.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author :Libi
 * @version :1.0
 * @date :2019-08-27 14:56
 */
@EnableEurekaClient
@SpringBootApplication
public class RibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }

    @LoadBalanced
    @Bean
    /**
     * 这里使用一个Rest模板帮我们调用服务的Rest接口，打上开启负载均衡的注解来开启负载均衡
     */
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
