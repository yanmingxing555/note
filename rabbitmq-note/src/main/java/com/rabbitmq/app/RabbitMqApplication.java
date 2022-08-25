package com.rabbitmq.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *
 *  @EnableDiscoveryClient 和 @EnableEurekaClient
 * 共同点就是:都是能够让注册中心能够发现,扫描到改服务。
 * 不同点:
 *      @EnableEurekaClient只适用于Eureka作为注册中心
 *      @EnableEurekaClient 可以是其他的注册中心
 */
@EnableDiscoveryClient
//@EnableEurekaClient//作用:注册服务到eurekaServer
@SpringBootApplication(scanBasePackages = "com.rabbitmq")
//@EnableHystrix//开启Hystrix断路器
//@EnableFeignClients({"com.eureka"})
public class RabbitMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApplication.class, args);
    }

}
