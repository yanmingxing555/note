package com.eureka.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/GatewayForward")
public class GatewayForwardController {
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/port1")
    public String queryPort1(){
        return "GatewayForward==>port1==>"+serverPort;
    }
    @GetMapping("/port2")
    public String queryPort2(){
        return "GatewayForward==>port2==>"+serverPort;
    }
}
