package com.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer//作用:开启eurekaServer
@SpringBootApplication(scanBasePackages = "com.eureka")
public class EurekaServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(EurekaServerApplication.class, args);
    }

}

/**
 * 什么是服务治理：
 *      在传统rpc远程调用中，服务与服务依赖关系，管理比较复杂，所以需要使用服务治理，
 *      管理服务与服务之间依赖关系，可以实现服务调用、负载均衡、容错等，实现服务发现与注册。
 * 服务注册与发现
 *      在服务注册与发现中，有一个注册中心，当服务器启动的时候，会把当前自己服务器的信息
 *          比如 服务地址通讯地址等以别名方式注册到注册中心上。
 *      另一方（消费者|服务提供者），以该别名的方式去注册中心上获取到实际的服务通讯地址，
 *          让后在实现本地rpc调用远程。
 */